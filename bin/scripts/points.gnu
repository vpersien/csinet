set term wxt 0

set palette defined ( \
    0 '#D53E4F',\
    1 '#F46D43',\
    2 '#FDAE61',\
    3 '#FEE08B',\
    4 '#E6F598',\
    5 '#ABDDA4',\
    6 '#66C2A5',\
    7 '#3288BD' )

#set xrange [1:0]
set yrange [1:0]

unset key
unset colorbox

#plot '../mean_meanings.csv' every ::5:2:5:3 w points ls 1
plot for [i=2:20:2] '../mean_meanings.csv' every ::30::31 u (column(i)/100):(column(i+1)/100) w points pt 5


pause -1
