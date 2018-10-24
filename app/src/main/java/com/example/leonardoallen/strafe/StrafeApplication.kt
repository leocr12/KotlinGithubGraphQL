package com.example.leonardoallen.strafe

import android.app.Application
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Field
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.cache.normalized.CacheKey
import com.apollographql.apollo.cache.normalized.CacheKeyResolver
import com.apollographql.apollo.cache.normalized.lru.EvictionPolicy
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCacheFactory
import com.apollographql.apollo.cache.normalized.sql.ApolloSqlHelper
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


class StrafeApplication: Application() {

    companion object {
        private const val BASE_URL = "https://api.githunt.com/graphql"
        private const val SQL_CACHE_NAME = "githuntdb"
        private lateinit var apolloClient: ApolloClient
    }

    override fun onCreate() {
        super.onCreate()

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val apolloSqlHelper = ApolloSqlHelper(this, SQL_CACHE_NAME)
        val normalizedCacheFactory = LruNormalizedCacheFactory(
            EvictionPolicy.NO_EVICTION,
            SqlNormalizedCacheFactory(apolloSqlHelper)
        )

        val cacheKeyResolver = object : CacheKeyResolver() {
            override fun fromFieldRecordSet(field: Field, map: Map<String, Any>): CacheKey {
                val typeName = map["__typename"] as String
                if ("User" == typeName) {
                    val userKey = typeName + "." + map["login"]
                    return CacheKey.from(userKey)
                }
                if (map.containsKey("id")) {
                    val typeNameAndIDKey = map["__typename"].toString() + "." + map["id"]
                    return CacheKey.from(typeNameAndIDKey)
                }
                return CacheKey.NO_KEY
            }

            override fun fromFieldArguments(field: Field, variables: Operation.Variables): CacheKey {
                return CacheKey.NO_KEY
            }
        }

        apolloClient = ApolloClient.builder()
            .serverUrl(BASE_URL)
            .okHttpClient(okHttpClient)
            .normalizedCache(normalizedCacheFactory, cacheKeyResolver)
            .build()
    }

    fun apolloClient(): ApolloClient {
        return apolloClient
    }

}