#set term tikz standalone color solid size 10cm,7cm header "\\usepackage{palatino,eulervm}"
#set output "./m1-merger.tex"

set style line 1 lw 3 linecolor rgb "#d6604d"
set style line 2 lw 3 linecolor rgb "#053061"
set style line 3 lw 3 linecolor rgb "#4393c3"
set style line 4 lw 3 linecolor rgb "#40004b"
set style line 5 lw 3 linecolor rgb "#67001f"

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

set arrow from 0,0.09 to 105,0.09 nohead front lc rgb 'black'
set arrow from 0,0.09 to 0,0.75 nohead front lc rgb 'black'
set arrow from 0,0.75 to 105,0.75 nohead front lc rgb 'black'
set arrow from 105,0.09 to 105,0.75 nohead front lc rgb 'black'

set label '$x_1$ of a' at 50,0.000 left front textcolor ls 1
set label '$x_2$ of a' at 50,0.071 left front offset 0,-0.2 textcolor ls 1
set label '$x_1$ of b' at 300,0.110 left front offset 0,-0.6 textcolor ls 2
set label '$x_2$ of b' at 300,0.114 left front textcolor ls 2
set label '$x_1$ of c' at 300,0.156 left front textcolor ls 3
set label '$x_2$ of c' at 300,0.852 left front offset 0,-0.75 textcolor ls 3
set label '$x_1$ of d' at 300,0.859 left front offset 0,-0.25 textcolor ls 4
set label '$x_2$ of d' at 300,0.183 left front offset 0,0.1 textcolor ls 4
set label '$x_1$ of e' at 300,0.870 left front offset 0,0.8 textcolor ls 5
set label '$x_2$ of e' at 300,0.869 left front offset 0,0.25 textcolor ls 5

plot	'm1-merger.csv' using ($1/1000):($2/100) title 'a' with lines ls 1,\
		'm1-merger.csv' using ($1/1000):($3/100) notitle with lines ls 1,\
		'm1-merger.csv' using ($1/1000):($4/100) title 'b' with lines ls 2,\
		'm1-merger.csv' using ($1/1000):($5/100) notitle with lines ls 2,\
		'm1-merger.csv' using ($1/1000):($6/100) title 'c' with lines ls 3,\
		'm1-merger.csv' using ($1/1000):($7/100) notitle with lines ls 3,\
		'm1-merger.csv' using ($1/1000):($8/100) title 'd' with lines ls 4,\
		'm1-merger.csv' using ($1/1000):($9/100) notitle with lines ls 4,\
		'm1-merger.csv' using ($1/1000):($10/100) title 'e' with lines ls 5,\
		'm1-merger.csv' using ($1/1000):($11/100) notitle with lines ls 5

pause -1
