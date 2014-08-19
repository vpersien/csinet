set output "m2-crosszipf-1-1-errors.tex"

set term tikz standalone color solid size 10cm,7cm header "\\usepackage{palatino,eulervm}"
#set term wxt 0


set yrange [0:100]
set xrange [0:250]

set ylabel "Success rate [percent per 1000 ticks]"
set xlabel "Generations [1k]"
#set xtics ('0' 0,'5' 5,'' 10,'20' 20,'25' 25,'30' 30,'35' 35,'40' 40)

unset key
unset colorbox

set style line 1 lw 2 linecolor rgb "#d6604d"
set style line 2 lw 1 linecolor rgb "#053061"
set style line 3 lw 1 linecolor rgb "#4393c3"
set style line 4 lw 1 linecolor rgb "#40004b"
set style line 5 lw 2 linecolor rgb "#67001f"


set label '$c_5a$' at 35,6.7 left front textcolor ls 1
#set label '$c_4a$' at 10,-1 left front rotate by -45 textcolor ls 1
set label '$c_1b$' at 59,44 left front textcolor ls 5
#set label '$c_2b$' at 15.8,-1 left front rotate by -45 textcolor ls 5
#set label '$c_3a$' at 17.2,-1 left front rotate by -45 textcolor ls 1
#
#set label '$c_2a$' at 13.3,82 left front textcolor ls 1
#set label '$c_3b$' at 16.3,93 left front textcolor ls 5



fname = "./m2-crosszipf-1-1-errorrates.csv"
color1 = "#d6604d"
color2 = "#67001f"

plot for [i=2:6] fname u ($1/1000):(column(i)*100) with points pt i%5 lw 2 lc rgb color1,\
     for [i=7:11] fname u ($1/1000):(column(i)*100) with points pt i%5 lw 2 lc rgb color2,\
     for [i=2:6] fname u ($1/1000):(column(i)*100) with lines ls 1,\
     for [i=7:11] fname u ($1/1000):(column(i)*100) with lines ls 5


#pause -1
