# 缓冲区溢出攻击
使用一段C代码进行缓冲区溢出攻击进行学习

## 环境
### 1.计算机平台：

32位的X86体系结构计算机+Windows XP SP3 操作系统

### 2.MinGW for C/C++ 编译器 （http://www.mingw.org/）

Minimalist GNU for Windows （简称MinGW）是一个开源的开发工具集，提供针对Mircosoft Windows本地程序应用的开发环境支持。它包含符合GNU标准的GCC编译器集合，包括C, C++, ADA and Fortran 编译器等。

安装注意事项：

* 安装路径中，不能存在空格（如不要安装在C:\Program Files\MinGW，因为Program Files目录名称中含有空格）。
建议安装在C:\MinGW或者其他磁盘分区的顶级目录等。
安装后可以查看C:\MinGW\Bin目录下的exe文件，包括gcc.exe, g++.exe和gdb.exe等。
* 安装结束后，设置系统用户环境变量：PATH = C:\MinGW\Bin。
其他事项：
http://www.mingw.org/wiki/Getting_Started http://www.mingw.org/wiki/InstallationHOWTOforMinGW

### 3.GNU的GDB 调试器
在安装MinGW后，在C:\MinGW\Bin目录中已经包含一个GDB调试器，即gdb.exe。

### 4.Code::Blocks C/C++（http://www.codeblocks.org/）
Code::Blocks是一个免费的C/C++程序开发集成环境，提供开发所必需的的支持。Code::Blocks基于插件框架设计和开发，具有高度可扩展和可配置的特点；其相关功能可通过添加插件的形式进行扩展，比如编译器和调试器。
在安装Code::Blocks后，需要设置其使用MinGW编译器和GDB调试器。
* 第一次启动Code::Blocks时，其会自动检测系统中已经安装的编译器。我们假设此时已经安装了MinGW编译器，因此，选择GNU MinGW Compiler。也可以通过设置“Settings->Compiler Settings->Toolchain executables”中的“Compiler’s installation directory”为“C:\MinGW”。
* 通过设置“Settings->Debugger settings”中的“GDB/CDB debugger – Default”选项中的“Executable path”为“C:\MinGW\bin\gdb.exe”。
  
## 内容
### 1.在Code::Blocks开发环境中，新建一个c工程项目。
* “File->New->Project”，选择建立Console application，点击next后选择C（语言程序），并点击next；
* 在“Project title”中填入“test”，在“Folder for create project in”中填入“C:\overflow”
### 2.编辑test工程中的main.c文件。输入程序：
```
#include <stdio.h>
#include<string.h>

void overflow(const char* input)
{
       char buf[8];
       printf("Virtual address of 'buf' = Ox%p\n", buf);
       strcpy(buf,input);
}

void fun()
{
    printf("Function 'fun' has been called without an explicitly invocation.\n");
    printf("Buffer Overflow attack succeeded!\n");
    // your other codes, e.g. deleting files
    // what happens when return?
}

int main(int argc, char* argv[])
{
        printf("Virtual address of 'overflow' = Ox%p\n",overflow);
        printf("Virtual address of 'fun' = Ox%p\n",fun);
        char input[]="AAAAAAAA";//good input, ASCII code of 'A' is 41
        //char input[]="AAAAAAAAAAAAAAAAAAAA\xB9\x13\x40\x00";//bad input
        overflow(input);
        return 0;
}
```
分析：编译并执行(Build->Build and Run)。
由于input没有超出buf的范围，程序正常执行。
程序输出函数overflow和fun的逻辑地址。

## 分析
[分析过程](https://github.com/HeXavi8/Info-Sec/edit/main/cache_overflow_attack/cache_overflow_attack.pdf)

## 思考
1. 解析实验过程中，最后一个图中返回值出现的原因（fun函数执行结束后，程序如何继续执行？）
2. 写一段存在缓冲区溢出攻击的程序；沿用上述攻击方法，实现对上述一个成功攻击，并给出详细过程（分析与图示）。
3. 了解目前编译器等对缓冲区溢出攻击的防护原理与方法，结合.net C/C++编译器的/GS（缓冲区安全检查）和/DYNAMICBASE（使用地址空间布局随机化功能）说明。
   
参考：

1.http://en.wikipedia.org/wiki/Buffer_overflow_protection
2.http://msdn.microsoft.com/en-us/library/aa290051(v=vs.71).aspx
3.http://msdn.microsoft.com/zh-cn/library/8dbf701c.aspx
4.http://blog.csdn.net/chengyun_chu/article/details/4644227
5.http://msdn.microsoft.com/zh-cn/library/bb384887.aspx
6.http://www.uml.org.cn/safe/201212174.asp

了解更多对软件代码缺陷的检测技术。


## 更多
可尝试这一段代码观察输出结果：[code](https://github.com/HeXavi8/Info-Sec/edit/main/cache_overflow_attack/code.c)
