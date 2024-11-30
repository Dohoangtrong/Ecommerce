// document.addEventListener("DOMContentLoaded", () => {
//     const passwordInput = document.getElementById("password");
//     const loginButton = passwordInput.parentNode.querySelector("button");
//     const errorMessage = document.createElement("div");
//     errorMessage.style.color = "red";
//     errorMessage.style.fontSize = "14px";
//     errorMessage.style.marginBottom = "10px";
//
//     // Chèn thông báo lỗi ngay trước nút Login
//     loginButton.parentNode.insertBefore(errorMessage, loginButton);
//
//     passwordInput.addEventListener("input", () => {
//         const password = passwordInput.value;
//
//         // Regex kiểm tra độ mạnh của mật khẩu
//         const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/;
//
//         if (!passwordRegex.test(password)) {
//             // Hiển thị thông báo lỗi nếu mật khẩu không hợp lệ
//             errorMessage.textContent = "Password must contain at least 8 characters, including uppercase, lowercase, and a number.";
//         } else {
//             // Xóa thông báo lỗi nếu mật khẩu hợp lệ
//             errorMessage.textContent = "";
//         }
//     });
// });
//
//
