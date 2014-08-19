#set output "../m2-coords.tex"

#set term tikz standalone color solid size 10cm,7cm header "\\usepackage{palatino,eulervm}"
set term wxt 0


set style line 1 lw 3 linecolor rgb "#d6604d"
set style line 2 lw 3 linecolor rgb "#053061"
set style line 3 lw 3 linecolor rgb "#4393c3"
set style line 4 lw 3 linecolor rgb "#40004b"
set style line 5 lw 3 linecolor rgb "#67001f"

set yrange [0.0:1.0]
#set xrange [0:150]
set xlabel "Generations [1k]"
set ylabel "Right"

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

plot	'../errorrates.csv' using ($1/1000):($2) title '0' with lines ls 1,\
		'../errorrates.csv' using ($1/1000):($3) title '1' with lines ls 2,\
		'../errorrates.csv' using ($1/1000):($4) title '2' with lines ls 3,\
		'../errorrates.csv' using ($1/1000):($5) title '3' with lines ls 4,\
		'../errorrates.csv' using ($1/1000):($6) title '4' with lines ls 5,\
		'../errorrates.csv' using ($1/1000):($7) title '5' with lines ls 1,\
		'../errorrates.csv' using ($1/1000):($8) title '6' with lines ls 2,\
		'../errorrates.csv' using ($1/1000):($9) title '7' with lines ls 3,\
		'../errorrates.csv' using ($1/1000):($10) title '8' with lines ls 4,\
		'../errorrates.csv' using ($1/1000):($11) title '9' with lines ls 5

#set output "../m2-means.tex"

#set term wxt 1
#
#set ylabel "Position"
#
#
#plot	'../meancoordinates.csv' using ($1/1000):($2/100) title '0' with lines ls 1,\
#		'../meancoordinates.csv' using ($1/1000):($3/100) notitle with lines ls 1,\
#		'../meancoordinates.csv' using ($1/1000):($4/100) title '1' with lines ls 2,\
#		'../meancoordinates.csv' using ($1/1000):($5/100) notitle with lines ls 2,\


set term wxt 2

set ylabel "Position"

plot '../quantiles.csv' using ($1/1000):($3/100):($2/100):($4/100) notitle with errorbars ls 1 lw 1,\
     '../quantiles.csv' using ($1/1000):($6/100):($5/100):($7/100) notitle with errorbars ls 1 lw 1,\
     '../quantiles.csv' using ($1/1000):($9/100):($8/100):($10/100) notitle with errorbars ls 2 lw 1,\
     '../quantiles.csv' using ($1/1000):($12/100):($11/100):($13/100) notitle with errorbars ls 2 lw 1
#f(i,q) = 2+(3*(i-1)+(q-1))
#plot for [i=1:4] '../quantiles.csv' u ($1/1000):(column(f(i,2))/100):(column(f(i,1))/100):(column(f(i,3))/100) with errorbars ls i lw 1

pause -1
