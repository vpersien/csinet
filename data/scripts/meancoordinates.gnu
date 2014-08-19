#set term tikz standalone color solid size 10cm,7cm header "\\usepackage{palatino,eulervm}"
#set output "../m1-stable.tex"

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
#set xrange [0:150]
set xlabel "Generations [1k]"
set ylabel "Position"

#set key outside below
set key off

#set label '$x_1$ of a' at 200,0.188 left front textcolor ls 1
#set label '$x_2$ of a' at 200,0.315 left front textcolor ls 1
#set label '$x_1$ of b' at 200,0.390 left front textcolor ls 2
#set label '$x_2$ of b' at 200,0.109 left front textcolor ls 2
#set label '$x_1$ of c' at 200,0.299 left front textcolor ls 3
#set label '$x_2$ of c' at 200,0.538 left front textcolor ls 3
#set label '$x_1$ of d' at 200,0.855 left front offset 0,0.3 textcolor ls 4
#set label '$x_2$ of d' at 200,0.537 left front textcolor ls 4
#set label '$x_1$ of e' at 200,0.495 left front textcolor ls 5
#set label '$x_2$ of e' at 200,0.847 left front offset 0,-0.3 textcolor ls 5

plot	'../meancoordinates.csv' using ($1/1000):($2/100) title 'a' with lines ls 1,\
		'../meancoordinates.csv' using ($1/1000):($3/100) notitle with lines ls 1,\
		'../meancoordinates.csv' using ($1/1000):($4/100) title 'b' with lines ls 2,\
		'../meancoordinates.csv' using ($1/1000):($5/100) notitle with lines ls 2,\
		'../meancoordinates.csv' using ($1/1000):($6/100) title 'c' with lines ls 3,\
		'../meancoordinates.csv' using ($1/1000):($7/100) notitle with lines ls 3,\
		'../meancoordinates.csv' using ($1/1000):($8/100) title 'd' with lines ls 4,\
		'../meancoordinates.csv' using ($1/1000):($9/100) notitle with lines ls 4,\
		'../meancoordinates.csv' using ($1/1000):($10/100) title 'e' with lines ls 5,\
		'../meancoordinates.csv' using ($1/1000):($11/100) notitle with lines ls 5
pause -1
