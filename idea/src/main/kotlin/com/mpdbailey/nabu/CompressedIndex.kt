package com.mpdbailey.nabu

import java.lang.IndexOutOfBoundsException

class CompressedIndex {
    companion object {
        fun flatten(indices : List<String>) = indices.fold(""){acc, s -> acc + s }
        fun unflatten(indexString : String) = indexString.chunked(3)
    }
    private val max = 'z'
    private val counter = charArrayOf('0','0','0')

    private val hashMap = HashMap<String, String>()

    private fun add(index : String){
        if (!hashMap.keys.contains(index)){
            hashMap[index] = next()
        }
    }

    fun createMap(indices : List<String>){
        indices.forEach { add(it) }
    }

    fun compress(index : String) = hashMap[index]!!

    fun compress(indices : List<String>) = indices
        .map{hashMap[it]!!}


    fun next() : String {
        counter[2]++
        if (counter[2] > max) {
            counter[2] = '0'
            counter[1]++
        }
        if (counter[1] > max) {
            counter[1] = '0'
            counter[0]++
        }
        if (counter[0] > max) {
            throw IndexOutOfBoundsException("Too many indices")
        }
        return String(counter)
    }
}