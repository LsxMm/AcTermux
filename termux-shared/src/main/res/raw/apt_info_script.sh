#!/bin/bash

# 列出已经订阅的 apt 仓库
subscribed_repositories() {
	local main_sources
	# 读取 sources.list 主列表中的仓库条目
	main_sources=$(grep -P '^\s*deb\s' "@TERMUX_PREFIX@/etc/apt/sources.list")

	if [ -n "$main_sources" ]; then
		echo "#### sources.list"
		echo "\`$main_sources\`"
	fi

	local filename repo_package supl_sources
	# 遍历 sources.list.d 目录下的每个文件
	while read -r filename; do
		# 查询该文件属于哪个包
		repo_package=$(dpkg -S "$filename" 2>/dev/null | cut -d : -f 1)
		# 获取该文件中的 deb 仓库条目
		supl_sources=$(grep -P '^\s*deb\s' "$filename")

		if [ -n "$supl_sources" ]; then
			if [ -n "$repo_package" ]; then
				# 打印文件及其归属包名
				echo "#### $repo_package (sources.list.d/$(basename "$filename"))"
			else
				# 只打印文件名
				echo "#### sources.list.d/$(basename "$filename")"
			fi
			echo "\`$supl_sources\`  "
		fi
	# 查找所有 sources.list.d 下的非目录文件进行处理
	done < <(find "@TERMUX_PREFIX@/etc/apt/sources.list.d" -maxdepth 1 ! -type d)
}

# 查询可升级的包
updatable_packages() {
	local updatable

	if [ "$(id -u)" = "0" ]; then
		# 如果是 root 用户则不执行
		echo "Running as root. Cannot check updatable packages."
	else
		# 更新软件包列表信息
		apt update >/dev/null 2>&1
		# 获取可升级的包列表，去掉首行 header
		updatable=$(apt list --upgradable 2>/dev/null | tail -n +2)

		if [ -z "$updatable" ];then
			# 如果没有可升级包
			echo "All packages up to date"
		else
			# 有可升级包，格式化输出
			echo $'```\n'"$updatable"$'\n```\n'
		fi
	fi
}

# 汇总最终输出
output="
### Subscribed Repositories

$(subscribed_repositories)
##


### Updatable Packages

$(updatable_packages)
##

"

# 打印输出
echo "$output"
