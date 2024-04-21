package com.vk.usersapp.core

object ViewModelProvider {

    private val featureStorage = hashMapOf<Class<*>, MVIFeature>()

    fun <T> obtainFeature (clazz: Class<T>, featureCreator: () -> MVIFeature): MVIFeature {
        return (featureStorage[clazz] ?: featureCreator().also { featureStorage[clazz] = it })
    }

    inline fun <reified T> obtainFeature(noinline featureCreator: () -> MVIFeature): T {
        return obtainFeature(T::class.java, featureCreator) as T
    }

    fun <T> destroyFeature(clazz: Class<T>) {
        featureStorage.remove(clazz)
    }
}