set output "m2-uniform2.tex"

set term tikz standalone color solid size 10cm,7cm header "\\usepackage{palatino,eulervm}"
#set term wxt 0


set yrange [0:1]
#set xrange [0:800]

set ylabel "Position"
set xlabel "Generations [1k]"

unset key
unset colorbox

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

set label 1 '$x_1$ of a' at 40,0.05 left front textcolor ls 1
set label 2 '$x_1$ of b' at 40,0.51 left front textcolor ls 5

set label 3 '$x_2$ of a' at 40,0.14 left front textcolor ls 1
set label 4 '$x_2$ of b' at 40,0.47 left front textcolor ls 5

fname = 'm2-uniform2-quantiles.csv'

plot fname u ($1/1000):($3/100) w lines ls 1,\
     fname u ($1/1000):($9/100) w lines ls 5,\
     fname every 4::2 u ($1/1000):($3/100):($2/100):($4/100) notitle with errorbars ls 1 lw 1 pt 0,\
     fname every 4::2 u ($1/1000):($9/100):($8/100):($10/100) notitle with errorbars ls 5 lw 1 pt 0,\
     fname u ($1/1000):($6/100) w lines ls 1,\
     fname u ($1/1000):($12/100) w lines ls 5,\
     fname every 4::4 u ($1/1000):($6/100):($5/100):($7/100) notitle with errorbars ls 1 lw 1 pt 0,\
     fname every 4::4 u ($1/1000):($12/100):($11/100):($13/100) notitle with errorbars ls 5 lw 1 pt 0




#set term wxt 1
#set output "m2-uniform-x2.tex"


#set ylabel "Position of $x_2$"
#set xlabel "Generations [1k]"


#set label 3 '$x_2$ of a' at 800,0.662 left front textcolor ls 5
#set label 4 '$x_2$ of b' at 800,0.174 left front textcolor ls 1


#fname = 'm2-uniform-quantiles.csv'

#plot fname u ($1/1000):($6/100) w lines ls 1,\
#     fname u ($1/1000):($12/100) w lines ls 5,\
#     fname every 20::20 u ($1/1000):($6/100):($5/100):($7/100) notitle with errorbars ls 1 lw 1 pt 0,\
#     fname every 20::20 u ($1/1000):($12/100):($11/100):($13/100) notitle with errorbars ls 5 lw 1 pt 0





#pause -1
