#!/bin/bash
str="http://localhost:90/"  # 要重复的字符串
result=""    # 用于存储重复后的字符串
for ((i=0; i<3000; i++)); do
  result="${result} ${str}"
done

echo 'done str'

curl  --http2 --parallel ${result} -v
wait

