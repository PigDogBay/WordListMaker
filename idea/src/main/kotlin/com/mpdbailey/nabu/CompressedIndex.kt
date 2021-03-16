package com.mpdbailey.nabu

class CompressedIndex {
    private val max = 'z'
    private val counter = charArrayOf('0','0','0')

    fun reset(){
        counter[0] = '0'
        counter[1] = '0'
        counter[2] = '0'
    }

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
            reset()
        }
        return String(counter)
    }
}