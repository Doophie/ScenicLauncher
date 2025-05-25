package ca.doophie.swipelauncher.data

import java.time.LocalTime

enum class DayPeriod {
    MORNING,
    AFTERNOON,
    EVENING,
    NIGHT
}

fun getDayPeriod(): DayPeriod {
    val hour = LocalTime.now().hour

    return if (hour in 4..11)
        DayPeriod.MORNING
    else if (hour in 12 .. 17)
        DayPeriod.AFTERNOON
    else if (hour in 18 .. 21)
        DayPeriod.EVENING
    else
        DayPeriod.NIGHT
}