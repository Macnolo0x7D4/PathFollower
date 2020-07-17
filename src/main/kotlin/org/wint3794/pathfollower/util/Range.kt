package org.wint3794.pathfollower.util

/**
 * Utility class for performing range operations
 */
object Range {
    //------------------------------------------------------------------------------------------------
    // Scaling
    //------------------------------------------------------------------------------------------------
    /**
     * Scale a number in the range of x1 to x2, to the range of y1 to y2
     * @param n number to scale
     * @param x1 lower bound range of n
     * @param x2 upper bound range of n
     * @param y1 lower bound of scale
     * @param y2 upper bound of scale
     * @return a double scaled to a value between y1 and y2, inclusive
     */
    fun scale(
        n: Double,
        x1: Double,
        x2: Double,
        y1: Double,
        y2: Double
    ): Double {
        val a = (y1 - y2) / (x1 - x2)
        val b = y1 - x1 * (y1 - y2) / (x1 - x2)
        return a * n + b
    }

    /**
     * clip 'number' if 'number' is less than 'min' or greater than 'max'
     * @param number number to test
     * @param min minimum value allowed
     * @param max maximum value allowed
     */
    fun clip(number: Double, min: Double, max: Double): Double {
        if (number < min) return min
        return if (number > max) max else number
    }

    /**
     * clip 'number' if 'number' is less than 'min' or greater than 'max'
     * @param number number to test
     * @param min minimum value allowed
     * @param max maximum value allowed
     */
    fun clip(number: Float, min: Float, max: Float): Float {
        if (number < min) return min
        return if (number > max) max else number
    }

    /**
     * clip 'number' if 'number' is less than 'min' or greater than 'max'
     * @param number number to test
     * @param min minimum value allowed
     * @param max maximum value allowed
     */
    fun clip(number: Int, min: Int, max: Int): Int {
        if (number < min) return min
        return if (number > max) max else number
    }

    /**
     * clip 'number' if 'number' is less than 'min' or greater than 'max'
     * @param number number to test
     * @param min minimum value allowed
     * @param max maximum value allowed
     */
    fun clip(number: Short, min: Short, max: Short): Short {
        if (number < min) return min
        return if (number > max) max else number
    }

    /**
     * clip 'number' if 'number' is less than 'min' or greater than 'max'
     * @param number number to test
     * @param min minimum value allowed
     * @param max maximum value allowed
     */
    fun clip(number: Byte, min: Byte, max: Byte): Byte {
        if (number < min) return min
        return if (number > max) max else number
    }
    //------------------------------------------------------------------------------------------------
    // Validation
    //------------------------------------------------------------------------------------------------
    /**
     * Throw an IllegalArgumentException if 'number' is less than 'min' or greater than 'max'
     * @param number number to test
     * @param min minimum value allowed
     * @param max maximum value allowed
     * @throws IllegalArgumentException if number is outside of range
     */
    @Throws(IllegalArgumentException::class)
    fun throwIfRangeIsInvalid(number: Double, min: Double, max: Double) {
        require(!(number < min || number > max)) {
            String.format(
                "number %f is invalid; valid ranges are %f..%f",
                number,
                min,
                max
            )
        }
    }

    /**
     * Throw an IllegalArgumentException if 'number' is less than 'min' or greater than 'max'
     * @param number number to test
     * @param min minimum value allowed
     * @param max maximum value allowed
     * @throws IllegalArgumentException if number is outside of range
     */
    @Throws(IllegalArgumentException::class)
    fun throwIfRangeIsInvalid(number: Int, min: Int, max: Int) {
        require(!(number < min || number > max)) {
            String.format(
                "number %d is invalid; valid ranges are %d..%d",
                number,
                min,
                max
            )
        }
    }
}