
-- Xóa database nếu tồn tại
IF DB_ID('ChefBookingDB') IS NOT NULL
BEGIN
    ALTER DATABASE ChefBookingDB SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE ChefBookingDB;
END
GO


CREATE DATABASE ChefBookingDB;
GO
USE ChefBookingDB;
GO


-- Bảng 1: Users (Người dùng)
-- Bảng này lưu tất cả tài khoản có thể đăng nhập hệ thống: Admin, Chef, và Customer.
-- Chúng ta dùng cột 'role' để phân biệt.
CREATE TABLE Users (
    user_id INT IDENTITY(1,1) PRIMARY KEY, -- ID tự tăng
    email NVARCHAR(255) NOT NULL UNIQUE, -- Dùng để đăng nhập, không trùng
    password_hash NVARCHAR(255) NOT NULL, -- Sẽ lưu mật khẩu đã băm (hash)
    full_name NVARCHAR(100) NOT NULL,
    phone_number VARCHAR(20) NULL,
    
    -- Phân quyền: 'ADMIN', 'CHEF', 'CUSTOMER'
    role VARCHAR(10) NOT NULL CHECK (role IN ('ADMIN', 'CHEF', 'CUSTOMER')),
    
    is_active BIT DEFAULT 1, -- Dùng để khóa tài khoản
    created_at DATETIME2 DEFAULT GETDATE() -- Ngày tạo tài khoản
);
GO

-- Bảng 2: Chef_Profiles (Hồ sơ đầu bếp)
-- Bảng này lưu thông tin chi tiết của đầu bếp.
-- Có quan hệ 1-1 với bảng Users (mỗi user 'CHEF' chỉ có 1 hồ sơ).
CREATE TABLE Chef_Profiles (
    -- Dùng chung ID với bảng Users (Quan hệ 1-1)
    chef_id INT PRIMARY KEY,
    
    bio NVARCHAR(MAX) NULL, -- Giới thiệu bản thân
    experience_years INT DEFAULT 0,
    location NVARCHAR(255) NULL, -- Khu vực hoạt động (ví dụ: 'Quận 1, TP.HCM')
    profile_image_url NVARCHAR(500) NULL,
    
    -- Trạng thái hồ sơ: 'PENDING' (chờ duyệt), 'APPROVED' (đã duyệt), 'REJECTED' (bị từ chối)
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED')),
    
    -- Khóa ngoại (Foreign Key) liên kết tới bảng Users
    CONSTRAINT FK_Chef_User FOREIGN KEY (chef_id) REFERENCES Users(user_id)
);
GO

-- Bảng 3: Menus (Thực đơn)
-- Mỗi đầu bếp có thể tạo nhiều thực đơn.
-- Quan hệ 1-N (1 Chef - N Menus)
CREATE TABLE Menus (
    menu_id INT IDENTITY(1,1) PRIMARY KEY,
    chef_id INT NOT NULL, -- Đầu bếp sở hữu menu này
    
    title NVARCHAR(200) NOT NULL, -- Tên thực đơn (ví dụ: 'Set Tiệc Âu 5 Món')
    description NVARCHAR(1000) NULL,
    
    -- Giá tiền tính trên mỗi khách
    price_per_person DECIMAL(18, 2) NOT NULL,
    
    min_guests INT DEFAULT 1, -- Số khách tối thiểu
    max_guests INT DEFAULT 100, -- Số khách tối đa
    menu_image_url NVARCHAR(500) NULL,
    
    -- Khóa ngoại liên kết tới bảng Chef_Profiles
    CONSTRAINT FK_Menu_Chef FOREIGN KEY (chef_id) REFERENCES Chef_Profiles(chef_id)
);
GO

-- Bảng 4: Bookings (Đơn đặt lịch)
-- Đây là bảng quan trọng nhất, lưu lại giao dịch đặt lịch.
CREATE TABLE Bookings (
    booking_id INT IDENTITY(1,1) PRIMARY KEY,
    
    customer_id INT NOT NULL, -- Người đặt (phải là role 'CUSTOMER')
    chef_id INT NOT NULL,     -- Đầu bếp được đặt
    menu_id INT NOT NULL,     -- Thực đơn được chọn
    
    booking_date DATETIME2 NOT NULL, -- Ngày giờ diễn ra bữa tiệc
    event_location NVARCHAR(500) NOT NULL, -- Địa chỉ tổ chức
    number_of_guests INT NOT NULL,
    
    -- Tổng tiền = price_per_person * number_of_guests (lưu lại để truy xuất nhanh)
    total_price DECIMAL(18, 2) NOT NULL,
    
    -- Trạng thái đơn: 'PENDING' (Chờ chef xác nhận), 'CONFIRMED' (Chef đồng ý), 'COMPLETED' (Hoàn thành), 'CANCELLED' (Bị hủy)
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'CONFIRMED', 'COMPLETED', 'CANCELLED')),
    
    notes NVARCHAR(1000) NULL, -- Ghi chú của khách hàng
    created_at DATETIME2 DEFAULT GETDATE(), -- Ngày tạo đơn
    
    -- Khóa ngoại
    CONSTRAINT FK_Booking_Customer FOREIGN KEY (customer_id) REFERENCES Users(user_id),
    CONSTRAINT FK_Booking_Chef FOREIGN KEY (chef_id) REFERENCES Chef_Profiles(chef_id),
    CONSTRAINT FK_Booking_Menu FOREIGN KEY (menu_id) REFERENCES Menus(menu_id)
);
GO

-- Bảng 5: Chef_Availability (Lịch rảnh/bận của đầu bếp)
-- Bảng này dùng để Chef đánh dấu ngày họ BẬN (không nhận khách).
-- Khi khách đặt, hệ thống sẽ kiểm tra xem ngày đó có trong bảng này không.
CREATE TABLE Chef_Availability (
    availability_id INT IDENTITY(1,1) PRIMARY KEY,
    chef_id INT NOT NULL,
    
    -- Ngày mà đầu bếp bận (chỉ cần lưu ngày, không cần giờ)
    unavailable_date DATE NOT NULL,
    
    reason NVARCHAR(255) NULL, -- (ví dụ: 'Nghỉ lễ', 'Việc cá nhân')
    
    -- Đảm bảo một đầu bếp không thể đánh dấu 1 ngày bận 2 lần
    UNIQUE(chef_id, unavailable_date), 
    
    -- Khóa ngoại
    CONSTRAINT FK_Availability_Chef FOREIGN KEY (chef_id) REFERENCES Chef_Profiles(chef_id)
);
GO

-- Bảng 6: Reviews (Đánh giá)
-- Khách hàng (Customer) đánh giá một đơn đặt (Booking) đã hoàn thành.
CREATE TABLE Reviews (
    review_id INT IDENTITY(1,1) PRIMARY KEY,
    
    -- Một đơn hàng chỉ được đánh giá 1 lần
    booking_id INT NOT NULL UNIQUE, 
    
    rating TINYINT NOT NULL CHECK (rating >= 1 AND rating <= 5), -- Sao (1 đến 5)
    comment NVARCHAR(1000) NULL,
    created_at DATETIME2 DEFAULT GETDATE(),
    
    -- Khóa ngoại
    CONSTRAINT FK_Review_Booking FOREIGN KEY (booking_id) REFERENCES Bookings(booking_id)
);
GO
