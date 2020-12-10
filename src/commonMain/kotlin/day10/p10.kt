package day10

import print

// 1984
// 3543369523456
val p10 = suspend {
    val ratings = input.lines().map { it.toInt() }
    val deviceRating = ratings.maxOf { it } + 3

    // Part 1
    (ratings + listOf(0, deviceRating))
        .sorted().zipWithNext()
        .map { it.second - it.first }
        .groupingBy { it }.eachCount()
        .print { "Part 1: ${it.getValue(1) * it.getValue(3)}" }


    // Part 2
    val ar = BooleanArray(deviceRating + 1) {
        when (it) {
            0 -> true
            deviceRating + 1 -> true
            else -> it in ratings
        }
    }
    val paths = Array<Long?>(deviceRating + 1) { null }.also { it[0] = 1L }

    fun countPaths(rating: Int): Long = paths[rating] ?: ((rating - 1) downTo (rating - 3)).filter { it >= 0 }
        .map { next -> (if (ar[next]) countPaths(next) else 0L).also { paths[next] = it } }
        .sum()

    countPaths(deviceRating).print { "Part 2: $it" }

}

class Adapter(val rating: Int)

val small = """16
10
15
5
1
11
7
19
6
12
4"""

val test = """28
33
18
42
31
14
46
20
48
47
24
23
49
45
19
38
39
11
1
32
25
35
8
17
7
9
4
2
34
10
3"""
val input = """38
23
31
16
141
2
124
25
37
147
86
150
99
75
81
121
93
120
96
55
48
58
108
22
132
62
107
54
69
51
7
134
143
122
28
60
123
82
95
14
6
106
41
131
109
90
112
1
103
44
127
9
83
59
117
8
140
151
89
35
148
76
100
114
130
19
72
36
133
12
34
46
15
45
87
144
80
13
142
149
88
94
61
154
24
66
113
5
73
79
74
65
137
47"""