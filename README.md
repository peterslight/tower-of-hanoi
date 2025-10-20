## Tower Of Hanoi

https://github.com/user-attachments/assets/428f10da-6eae-4e4a-9cfe-cded2fb44bcf

mini game to showcase the tower of hanoi problem that is often solved using a recursive approach.
the game involves moving disks one at a time from one tower to another until all disks have been moved to the last tower and are arranged in ascending order.
this game is particularly interesting because it shows the power of exponents to the power of 2, since for every time we add an extra disk the number of moves at least doubles.

Formula (3 towers): M(n)=2^n-1 (optimal solution)

2 disks → 3 moves
3 disks → 7 moves
4 disks → 15 moves
5 disks → 31 moves
6 disks → 63 moves
7 disks → 127 moves
8 disks → 255 moves
9 disks → 511 moves
10 disks → 1,023 moves

this can also be solved algorithmically with a time complexity of O(2ⁿ)

