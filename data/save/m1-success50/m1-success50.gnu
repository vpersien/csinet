set term tikz standalone color solid size 10cm,7cm header "\\usepackage{palatino,eulervm}"

set output "./m1-success50-1.tex"
#set term x11 0

set style line 5 lw 3 linecolor rgb "#67001f"
set style line 4 lw 3 linecolor rgb "#40004b"
set style line 1 lw 3 linecolor rgb "#d6604d"
set style line 2 lw 3 linecolor rgb "#053061"
set style line 3 lw 3 linecolor rgb "#4393c3"

set palette model RGB defined (\
	2	"#8e0152",\
	4	"#de77ae",\
	6	"#b8e186",\
	8	"#4d9221",\
	10	"#000000"\
)

set yrange [0.0:1.0]
set xrange [0:100]
set xlabel "Generations [1k]"
set ylabel "Position"

#set key outside below
set key off

#set style rectangle fc rgb "red" fs solid 0.5 border -1
#set object 1 rect from 82.000,0.17 to 122,0.36 front lt 2

set arrow 1 from 31,0.18 to 41.3,0.18 nohead front lc rgb 'black'
set arrow 2 from 31,0.18 to 31,0.24 nohead front lc rgb 'black'
set arrow 3 from 31,0.24 to 41.3,0.24 nohead front lc rgb 'black'
set arrow 4 from 41.3,0.18 to 41.3,0.24 nohead front lc rgb 'black'

set label 1 '$x_1$ of a' at 100,0.744 left front textcolor ls 1
set label 2 '$x_2$ of a' at 100,0.167 left front offset 0,0.5 textcolor ls 1
set label 3 '$x_1$ of b' at 100,0.159 left front textcolor ls 2
set label 4 '$x_2$ of b' at 100,0.154 left front offset 0,-0.5 textcolor ls 2

plot	'm1-success50.csv' using ($1/1000):($2/100) title 'a' with lines ls 1,\
		'm1-success50.csv' using ($1/1000):($3/100) notitle with lines ls 1,\
		'm1-success50.csv' using ($1/1000):($4/100) title 'b' with lines ls 2,\
		'm1-success50.csv' using ($1/1000):($5/100) notitle with lines ls 2,\
#		'm1-success50.csv' using ($1/1000):($8/100) title 'd' with lines ls 4,\
#		'm1-success50.csv' using ($1/1000):($9/100) notitle with lines ls 4,\
#		'm1-success50.csv' using ($1/1000):($10/100) title 'e' with lines ls 5,\
#		'm1-success50.csv' using ($1/1000):($11/100) notitle with lines ls 5,\
#		'm1-success50.csv' using ($1/1000):($6/100) title 'c' with lines ls 3,\
#		'm1-success50.csv' using ($1/1000):($7/100) notitle with lines ls 3


set output "./m1-success50-2.tex"
#set term x11 1

set xrange [0:400]

unset arrow 1
unset arrow 2
unset arrow 3
unset arrow 4

set arrow from 110.5,0.255 to 110.5,0.74 nohead front lw 3 lc rgb 'black'

unset label 1
unset label 2
unset label 3
unset label 4

set label '$x_1$ of a' at 400,0.592 left front offset 0,-0.5 textcolor ls 1
set label '$x_2$ of a' at 400,0.600 left front textcolor ls 1
#set label '$x_1$ of b' at 400,0.144 left front textcolor ls 2
#set label '$x_2$ of b' at 400,0.156 left front textcolor ls 2
#set label '$x_1$ of c' at 400,0.676 left front offset 0,0.5 textcolor ls 3
#set label '$x_2$ of c' at 400,0.628 left front offset 0,0.1 textcolor ls 3
set label '$x_1$ of d' at 400,0.765 left front offset 0,0.2 textcolor ls 4
set label '$x_2$ of d' at 400,0.643 left front offset 0,0.0 textcolor ls 4
#set label '$x_1$ of e' at 400,0.597 left front offset 0,-0.5 textcolor ls 5
#set label '$x_2$ of e' at 400,0.698 left front offset 0,0.7 textcolor ls 5

plot	'm1-success50.csv' using ($1/1000):($2/100) title 'a' with lines ls 1,\
		'm1-success50.csv' using ($1/1000):($3/100) notitle with lines ls 1,\
		'm1-success50.csv' using ($1/1000):($8/100) title 'd' with lines ls 4,\
		'm1-success50.csv' using ($1/1000):($9/100) notitle with lines ls 4
#		'm1-success50.csv' using ($1/1000):($6/100) title 'c' with lines ls 3,\
#		'm1-success50.csv' using ($1/1000):($7/100) notitle with lines ls 3,\
#		'm1-success50.csv' using ($1/1000):($10/100) title 'e' with lines ls 5,\
#		'm1-success50.csv' using ($1/1000):($11/100) notitle with lines ls 5
#		'm1-success50.csv' using ($1/1000):($4/100) title 'b' with lines ls 2,\
#		'm1-success50.csv' using ($1/1000):($5/100) notitle with lines ls 2,\



#pause -1
