# 记账APP - Salary2

## 介绍

这是给妈妈做的一个记录每天缝纫衣服的款式及数量的手机应用，便于年底对账。

APP 采用 Android Studio 开发，相比于2020年做的第一代，添加了三个需求：

* 添加每日记录列表，便于查看每日录入数据是否正常，并计算一天所做的所有衣服总价
* 能够随时修改某一款式的单价
* 夜间过零点之后，早上五点前，录入数据时自动填入的日期仍视为昨天

apk下载链接：https://www.pgyer.com/jxgW，或扫描下方二维码即可安装。

<img src="https://img-blog.csdnimg.cn/fd024a743d924c10ab414bb24af37c45.png" style="float:left" width="15%">

## 界面设计

<div>
    <img src="https://img-blog.csdnimg.cn/cf42dadf77ac42f3afe1f3305328b135.png?" width="24%" style="position:left">
    <img src="https://img-blog.csdnimg.cn/9c63fb2ed7df41879edf54f7b2bafb03.png?" width="24%" style="positon:left">
    <img src="https://img-blog.csdnimg.cn/3f75f53f9fee44c78b4157ab86084450.png?" width="24%" style="position:left">
    <img src="https://img-blog.csdnimg.cn/6ba38ed2f08b485ab45b3f446ac7fe51.png?" width="24%" style="position:left">
</div>


## 数据库

数据库用的是 Android Studio 自带的 SQLite 构建，其创建的数据表语句如下：

```sql
CREATE TABLE style(
styleid INTEGER PRIMARY KEY AUTOINCREMENT,
stylename VARCHAR(20),
price DECIMAL(8, 2))
```

款式表，记录每个款式的款式名及单价

```sql
CREATE TABLE record(
recordid INTEGER PRIMARY KEY AUTOINCREMENT,
styleid INTEGER,
number INTEGER,
time DATE,
isdel INTEGER
remark VARCHAR(255))
```

记录表，记录每一条录入数据的款式编号、数量、时间及备注，删除记录时 isdel 会被标为 1，以免错误操作造成不可挽回的后果。