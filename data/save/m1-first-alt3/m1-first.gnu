set term tikz standalone color solid size 10cm,7cm header "\\usepackage{palatino,eulervm}"
set output "./m1-first.tex"

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
#set xrange [0:100]
set xlabel "Generations [1k]"
set ylabel "Position"

#set key outside below
set key off

set label '$x_1$ of a' at 100,0.350 left front offset 0,0.3 textcolor ls 1
set label '$x_2$ of a' at 100,0.333 left front textcolor ls 1
set label '$x_1$ of b' at 100,0.170 left front textcolor ls 2
set label '$x_2$ of b' at 100,0.164 left front offset 0,-0.5 textcolor ls 2
set label '$x_1$ of c' at 100,0.212 left front textcolor ls 3
set label '$x_2$ of c' at 100,0.754 left front offset 0,-0.5 textcolor ls 3
set label '$x_1$ of d' at 100,0.794 left front offset 0,0.6 textcolor ls 4
set label '$x_2$ of d' at 100,0.221 left front offset 0,0.5 textcolor ls 4
set label '$x_1$ of e' at 100,0.764 left front textcolor ls 5
set label '$x_2$ of e' at 100,0.783 left front offset 0,0.3 textcolor ls 5

plot	'm1-first.csv' using ($1/1000):($2/100) title 'a' with lines ls 1,\
		'm1-first.csv' using ($1/1000):($3/100) notitle with lines ls 1,\
		'm1-first.csv' using ($1/1000):($4/100) title 'b' with lines ls 2,\
		'm1-first.csv' using ($1/1000):($5/100) notitle with lines ls 2,\
		'm1-first.csv' using ($1/1000):($6/100) title 'c' with lines ls 3,\
		'm1-first.csv' using ($1/1000):($7/100) notitle with lines ls 3,\
		'm1-first.csv' using ($1/1000):($8/100) title 'd' with lines ls 4,\
		'm1-first.csv' using ($1/1000):($9/100) notitle with lines ls 4,\
		'm1-first.csv' using ($1/1000):($10/100) title 'e' with lines ls 5,\
		'm1-first.csv' using ($1/1000):($11/100) notitle with lines ls 5

#pause -1
