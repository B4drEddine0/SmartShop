# SmartShop - B2B Commerce Management System

## 📋 Project Overview

**SmartShop** is a comprehensive B2B (Business-to-Business) commerce management system designed specifically for **MicroTech Maroc**, a leading technology distributor in Morocco. The platform manages a network of **650+ commercial clients** and streamlines the entire order-to-delivery workflow with advanced customer loyalty programs and flexible payment solutions.

### 🎯 Business Context

SmartShop was developed to address the complex requirements of managing:
- **650+ Commercial Clients** across Morocco
- **Multi-tier Loyalty Program** (SILVER, GOLD, PLATINUM tiers)
- **Diverse Payment Methods** (Card, Check, Wire Transfer, Cash)
- **B2B Commerce Operations** with wholesale pricing and bulk ordering
- **Session-based Authentication** for secure client access

---

## ✨ Core Features

### 1. **Customer Management**
- Client registration and profile management
- Company details and contact information
- Multi-tier loyalty system (SILVER, GOLD, PLATINUM)
- Client classification and segmentation

### 2. **Loyalty Tiers System**
- **SILVER Tier**: Base level membership for all clients
- **GOLD Tier**: Mid-level with enhanced benefits and discounts
- **PLATINUM Tier**: Premium tier for high-value clients
- Automatic tier upgrades based on purchase history
- Tier-specific pricing and benefits

### 3. **Order Management**
- Browse and search product catalog
- Wholesale pricing structures
- Bulk ordering capabilities
- Order history and tracking
- Order status monitoring

### 4. **Multi-Payment Integration**
- **Credit Card** payments with secure processing
- **Check** payments with verification
- **Wire Transfer** integration for large orders
- **Cash on Delivery** option
- Payment method flexibility by client tier

### 5. **Product Catalog**
- Comprehensive product database
- Category-based organization
- Pricing by customer tier
- Stock availability tracking
- Product specifications and details

### 6. **User Authentication & Security**
- HTTP Session-based authentication
- Secure login/logout functionality
- Session management for concurrent users
- Role-based access control
- Client data protection

### 7. **Reporting & Analytics**
- Sales performance dashboards
- Client activity reports
- Order analytics by tier
- Revenue tracking
- Loyalty program metrics

---

## 🛠️ Technology Stack

### Backend
- **Framework**: Spring Boot (Java)
- **Architecture**: MVC Pattern with Repository Layer
- **Authentication**: HTTP Session Management
- **Database**: Relational Database (MySQL/PostgreSQL compatible)
- **Build Tool**: Maven

### Frontend
- **Template Engine**: Thymeleaf for server-side rendering
- **Styling**: Bootstrap & Custom CSS
- **JavaScript**: Client-side validation and interactions

### Key Dependencies
- Spring Data JPA for database operations
- Spring Security for session management
- Spring Web for REST/MVC endpoints
- Validation frameworks for data integrity

---

## 📦 Project Structure

```
SmartShop/
├── src/main/java/
│   └── [Package Structure]
│       ├── controller/          # Web controllers for handling requests
│       ├── service/             # Business logic layer
│       ├── repository/          # Data access layer
│       ├── model/               # Entity classes (Client, Order, Product, Payment)
│       └── config/              # Spring configuration
├── src/main/resources/
│   ├── templates/               # Thymeleaf HTML templates
│   └── application.properties   # Configuration settings
├── pom.xml                      # Maven dependencies
└── README.md                    # This file
```

---

## 🚀 Getting Started

### Prerequisites
- Java 11 or higher
- Maven 3.6+
- MySQL/PostgreSQL database
- Git

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/B4drEddine0/SmartShop.git
   cd SmartShop
   ```

2. **Configure Database**
   - Update `src/main/resources/application.properties`
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/smartshop
   spring.datasource.username=root
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```

3. **Build the Project**
   ```bash
   mvn clean install
   ```

4. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

5. **Access the Application**
   - Open your browser and navigate to: `http://localhost:8080`

---

## 👥 User Roles & Access Control

### Admin
- Manage all clients and orders
- Configure loyalty tiers and pricing
- Access system reports and analytics
- User management

### Client
- Browse product catalog
- Place and track orders
- View loyalty tier status
- Manage payment methods
- Access order history

### Sales Representative
- Manage assigned client accounts
- Create orders on behalf of clients
- View client analytics
- Generate sales reports

---

## 💳 Payment Methods

| Payment Method | Description | Tier Availability |
|---|---|---|
| **Credit Card** | Secure online payment | All Tiers |
| **Check** | Traditional check payment | All Tiers |
| **Wire Transfer** | Bank transfer for bulk orders | GOLD, PLATINUM |
| **Cash** | Cash on delivery | SILVER, GOLD |

---

## 🎁 Loyalty Program Benefits

### SILVER (Base Tier)
- Standard wholesale pricing
- Basic order management
- Email support
- Order history access

### GOLD (Mid Tier)
- 5-10% price discount on all products
- Priority order processing
- Dedicated account manager
- Monthly performance reports
- Free shipping on orders > 5000 DH

### PLATINUM (Premium Tier)
- 15-20% price discount on all products
- Priority 24/7 support
- Custom pricing negotiations
- Quarterly business reviews
- Free shipping on all orders
- Exclusive product previews
- Custom order scheduling

---

## 📊 Database Schema Overview

### Key Entities
- **Client**: Stores client company information and tier level
- **Product**: Product catalog with pricing
- **Order**: Order details and status tracking
- **OrderItem**: Individual products in each order
- **Payment**: Payment records and methods
- **User**: Login credentials and authentication

---

## 🔐 Security Features

✅ **HTTP Session-based Authentication**: Secure user identification
✅ **Password Encryption**: Bcrypt password hashing
✅ **Session Management**: Automatic session timeout
✅ **CSRF Protection**: Spring Security CSRF tokens
✅ **Data Validation**: Input validation on all forms
✅ **Role-Based Access Control**: URL-level security

---

## 📝 API Endpoints (Examples)

```
Authentication
POST    /login              Login to the system
GET     /logout             Logout from the system

Clients
GET     /clients            List all clients
GET     /clients/{id}       Get client details
POST    /clients            Create new client
PUT     /clients/{id}       Update client

Orders
GET     /orders             List user orders
POST    /orders             Create new order
GET     /orders/{id}        Get order details
PUT     /orders/{id}        Update order status

Products
GET     /products           List all products
GET     /products/{id}      Get product details

Payments
POST    /payments           Process payment
GET     /payments/{id}      Get payment details
```

---

## 🧪 Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn test -Dtest=*IT
```

---

## 📈 Performance Optimization

- Database query optimization with proper indexing
- Lazy loading for related entities
- Caching strategies for frequently accessed data
- Session optimization for high-traffic scenarios
- Pagination for large datasets

---

## 🔄 Deployment

### Production Build
```bash
mvn clean package -DskipTests
```

### Deployment Options
- **Standalone JAR**: `java -jar smartshop.jar`
- **Application Server**: Deploy WAR to Tomcat/Jetty
- **Docker**: Container deployment with Docker Compose

---

## 📞 Support & Contact

- **Repository**: [GitHub - SmartShop](https://github.com/B4drEddine0/SmartShop)
- **Issues**: Report bugs and request features via GitHub Issues
- **Developer**: B4drEddine0

---

## 📄 License

This project is licensed under the MIT License. See the LICENSE file for details.

---

## 🎓 Contributing

Contributions are welcome! Please follow these steps:
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

**Last Updated**: 2024
**Maintained by**: B4drEddine0
**Status**: ✅ Active Development
