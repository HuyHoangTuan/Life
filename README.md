** Chú ý: Làm theo chỉ dẫn để có thể cài đặt hệ thống một cách dễ dàng và nhanh chóng nhất. Ngoài ra nếu có vấn đề phastt sinh, xem phần lưu ý để có thể chỉnh sửa. 
# 1. Khởi tạo database
Yêu cầu: Dbeaver phiên bản 21.3.0 và PostgreSQL 14. Trong quá trình cài đặt PostgreSQL cần lưu ý username, port, master password và server password của server.
-	Bước 1: Mở terminal tại một folder bất kỳ.
-	Bước 2: Tại terminal: “mkdir Life”: khởi tạo thư mục project.
-	Bước 3: Terminal: “cd Life”, “mkdir main”: khởi tạo thư mục main trong Life.
-	Bước 4: Terminal: ”cd main” ”git clone https://github.com/HuyHoangTuan/Life” : clone project về thư mục main
-	Bước 5: Terminal: “cd Life”, “git checkout main”: chọn folder main làm nhánh main.
-	Bước 6: Sau khi cài đặt PostgreSQL, mở PGAdmin4. Nhập master password để truy cập vào hệ thống.
-	Bước 7: Chọn server là PostgreSQL, nhập server password để có thể truy cập vào trang quản lý database.
-	Bước 8: Tại Database, chọn Create/Database… Đặt tên cho Database là Life, sau đó bấm Save.
-	Bước 9: Tại DBeaver, chọn Database/New Database Connection, sau đó chọn hệ quản trị PostgreSQL. Khi đó Dbeaver sẽ mở ra hộp thoại nhập các thông tin về server cần kết nối. Điền đầy đủ các thông tin về Host (localhost), Port, Database (Life), Username và Password. Sau đó bấm Test Connection… để kiểm tra kết nối. Nếu kết nối không thành công, cần kiểm tra lại các trường thông tin xem đã đúng hay chưa.
-	Bước 10: Chuột phải vào file  Life/Databases/Life, chọn Tools/Restore. Chọn Format là Tar, tích chọn 2 tùy chọn “Clean (drop) database objects before recreating  them” và “Discard objects owner”. Chọn file backup là file dump-test-202202242053.tar nằm trong file main đã clone về trước đó. Sau đó bấm Start để máy tự cấu hình database. Sau khi cấu hình xong máy có thể sẽ xuất hiện cảnh báo. Ta có thể bỏ qua cảnh báo đó.

# 2. Khởi chạy BackEnd
Yêu cầu: IntelliJ community version 213.6461.79, JDK version 17, môi trường Maven để cấu hình và khởi chạy BackEnd.
-	Bước 1: Mở terminal tại folder Life ở phần trên.
-	Bước 2: Tại terminal: “mkdir be”: tạo folder be.
-	Bước 3: Terminal: “cd be”, “git clone https://github.com/HuyHoangTuan/Life”: clone project về folder be.
-	Bước 4: Terminal: “cd Life”, “git checkout develop-backend”: chọn folder be là nhánh của “develop-backend”
-	Bước 5: Trong intelliJ, chọn mở thư mục Life. Sau đó bấm chuột phải vào nó, chọn Maven và chọn Reload project.
-	Bước 6: Chỉnh sửa các giá trị “spring.datasource.url”, “spring.datasource.username” và “spring.datasource.password” trong file application.properties nằm trong thư mục Life\src\main\java\resources sao cho phù hợp.
-	Bước 7: Chọn Add Configurations, chọn “com.example.Life.LifeApplication” làm main class, sau đó Apply và OK.
-	Bước 8: Bấm “Run ‘LifeApplication’ ”. Trường “Name” đặt tên “LifeApplication”, main class chọn “com.example.Life.LifeApplication”. Sau đó apply và bấm OK.
-	Bước 9: Khởi chạy file “LifeApplication”.
** Chú ý: Nếu xuất hiện lỗi “Port 8080 was already in use.”: cần đổi lại giá trị server.port trong file Life\src\main\java\resources\application.properties. Sau đó tại front end cũng cần đổi giá trị cổng để hệ thống có thể vận hành.
# 3. Khởi chạy FrontEnd
Yêu cầu: NodeJS phiên bản 16.13.0
-	Bước 1: Mở terminal tại folder Life ở phần trên.
-	Bước 2: Tại terminal: “midir fe”: tạo folder fe.
-	Bước 3: Terminal: “cd fe”, “git clone https://github.com/HuyHoangTuan/Life”: clone project về folder fe.
-	Bước 4: Terminal: “cd Life”, “git checkout develop-backend”: chọn folder Fe là nhánh của “develop-backend”
-	Bước 5: Terminal: “npm install”, “npm run start”: 
** Chú ý: Nếu đổi Port của server Backend khác 8080, cần đổi port tại biến APIhost trong Life\front-end\sources\api.js
# 4. Phân công công việc
## Hoàng Tuấn Huy - 20180099 - Viết backEnd, hỗ trợ viết frontEnd, hỗ trợ thiết kế hệ thống
## Dương Trung Hiếu - 20183740 - Thiết kế hệ thống, thực hiện báo cáo, hỗ trợ biết FrontEnd.
## Trần Minh Công - 20180032 - Viết frontEnd.
## Nguyễn Phúc Tân - 20183824 - Hỗ trợ làm backEnd, frontEnd.

# Một số tài khoản đã được tạo trước trong database:
| Tên tài khoản | Mật khẩu   | Vai trò |
| ---           | ---        | ---     |
| admin         | admin      | Admin   |
| ---           | ---        | ---     |
| taylorswift   | 123        | Artist  |
| ---           | ---        | ---     |
| brunomars     | 123        | Artist  |
| ---           | ---        | ---     |
| fankychop@gmail.com | 123 | Client |