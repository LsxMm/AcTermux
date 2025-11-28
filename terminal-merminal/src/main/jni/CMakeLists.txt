# 设置 CMake 最低版本 (Android Studio 默认通常支持 3.10.2+)
cmake_minimum_required(VERSION 3.10.2)

# 项目名称
project(merminal)

# 1. 定义源文件和库类型 (LOCAL_MODULE & LOCAL_SRC_FILES)
# SHARED 表示生成 .so 动态库
add_library(merminal SHARED
        merminal.cpp)

# 2. 设置头文件路径 (LOCAL_C_INCLUDES)
target_include_directories(merminal PRIVATE
        ${CMAKE_CURRENT_SOURCE_DIR})

# 3. 设置 C++ 标准 (对应 -std=c++17)
set_target_properties(merminal PROPERTIES
        CXX_STANDARD 17
        CXX_STANDARD_REQUIRED ON)

# 4. 编译选项 (LOCAL_CFLAGS & LOCAL_CPPFLAGS)
# 注意：LOCAL_ARM_MODE := arm 对应 -marm
target_compile_options(merminal PRIVATE
        -Wno-error=format-security
        -fvisibility=hidden
        -ffunction-sections
        -fdata-sections
        -w
        -Werror
        -fno-rtti
        -fno-exceptions
        -fpermissive
        -fms-extensions
        -Wno-error=c++11-narrowing
        $<IF:$<STREQUAL:$<TARGET_PROPERTY:merminal,ANDROID_ABI>,armeabi-v7a>, -marm, >
        )

# 5. 链接选项 (LOCAL_LDFLAGS)
target_link_options(merminal PRIVATE
        -Wl,--gc-sections
        -Wl,--strip-all # 对应 --strip-all 和 -s
        )

# 6. 链接系统库 (LOCAL_LDLIBS)
# 查找 log 库
find_library(log-lib log)

# 链接库 (-llog -landroid -lGLESv2)
target_link_libraries(merminal
        ${log-lib}
        android
        GLESv2)
