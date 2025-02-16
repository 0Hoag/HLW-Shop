# 📱 E-Commerce Platform  
A modern web-based platform designed for seamless online shopping, offering a user-friendly experience with secure transactions and an intuitive interface.

## 🚀 About  
E-Commerce Platform is a full-stack application that provides a robust and scalable online marketplace. It enables users to browse products, add them to a cart, complete secure transactions, and manage their orders efficiently. With a microservices architecture, this project ensures high scalability and future enhancements.

## ✨ Features  
- User authentication with password and multi-platform OAuth 2.0 (Google, GitHub)
- Product browsing, searching, and filtering with detailed descriptions
- Shopping cart management (add, remove, update quantity)
- Secure checkout with integrated payment gateways (VNPay)
- Order tracking and management with real-time updates
- Wishlist functionality to save favorite products
- Admin dashboard for product, order, and user management
- Cloud storage for product images via Cloudinary
- Event-driven architecture with Apache Kafka for messaging

## 🛠️ Technologies  
- **Backend**: Spring Boot  
- **Database**: PostgreSQL, MongoDB  
- **Authentication**: JWT (JSON Web Tokens), OAuth 2.0  
- **Real-time Features**: WebSocket  
- **Messaging & Event Streaming**: Apache Kafka  
- **Payment Integration**: VNPay  
- **Cloud Storage**: Cloudinary  

## 📦 Installation  
- **Clone the repository**:
```bash
  git clone https://github.com/0Hoag/HLW-Shop.git
  cd ecommerce-platform
```
- **Backend Setup**:
```bash
  cd backend
  ./mvnw clean install
  ./mvnw spring-boot:run
```
