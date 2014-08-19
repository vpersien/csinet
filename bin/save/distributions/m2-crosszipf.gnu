set output "m2-crosszipf-hist.tex"

set term tikz standalone color solid size 10cm,7cm header "\\usepackage{palatino,eulervm}"

#set yrange [0:0.6]
set xrange [0.5:5.5]

set bmargin 2.5

unset key
unset colorbox
unset xtics

set style line 1 lw 3 linecolor rgb "#d6604d"
set style line 2 lw 3 linecolor rgb "#053061"
set style line 3 lw 3 linecolor rgb "#4393c3"
set style line 4 lw 3 linecolor rgb "#40004b"
set style line 5 lw 3 linecolor rgb "#67001f"

set palette maxcolors 5
set palette model RGB defined (\
   1 "#d6604d",\
   2 "#053061",\
   3 "#4393c3",\
   4 "#40004b",\
   5 "#67001f"\
)

set label '$m_1$' at 1-0.15,-0.0225 center front textcolor ls 1
set label '$m_2$' at 2-0.15,-0.0225 center front textcolor ls 1
set label '$m_3$' at 3-0.15,-0.0225 center front textcolor ls 1
set label '$m_4$' at 4-0.15,-0.0225 center front textcolor ls 1
set label '$m_5$' at 5-0.15,-0.0225 center front textcolor ls 1

set label '$m_6$' at 1+0.15,-0.0225 center front textcolor ls 5
set label '$m_7$' at 2+0.15,-0.0225 center front textcolor ls 5
set label '$m_8$' at 3+0.15,-0.0225 center front textcolor ls 5
set label '$m_9$' at 4+0.15,-0.0225 center front textcolor ls 5
set label '$m_{10}$' at 5+0.15,-0.0225 center front textcolor ls 5

set label '$c_1$' at 1,-0.054 center front textcolor rgb 'black'
set label '$c_3$' at 3,-0.054 center front textcolor rgb 'black'
set label '$c_2$' at 2,-0.054 center front textcolor rgb 'black'
set label '$c_4$' at 4,-0.054 center front textcolor rgb 'black'
set label '$c_5$' at 5,-0.054 center front textcolor rgb 'black'

set samples 5
set style fill solid 1.0 border
set boxwidth 0.3 relative
q = 3
k = 5
N = k+k*q

#plot 'dualzipf.csv' u (column(0)+0.1):$1:1 w boxes palette,\
#     'dualzipf.csv' u (column(0)-0.1):$2:2 w boxes palette

plot 'crosszipf.csv' u ($1+0.1):4:($2+1) w boxes palette,\
     'crosszipf.csv' u ($1-0.1):3:2 w boxes palette

#pause -1
