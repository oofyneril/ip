# Storage format

Peggy saves tasks to `data/peggy.txt` in the format:

`T | 1 | read book`
`D | 0 | return book | June 6th`
`E | 0 | project meeting | Aug 6th 2-4pm`

- `T` = todo, `D` = deadline, `E` = event
- `1` = done, `0` = not done
