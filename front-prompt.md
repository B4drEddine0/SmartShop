# SmartShop — Frontend UI Generation Prompt

You are an expert frontend developer. Generate a **complete, production-ready frontend application** for the SmartShop backend API described below. This is a B2B commercial management platform for **MicroTech Maroc**, a computer hardware distributor based in Casablanca, managing 650+ active clients with a loyalty tier system, split payments, and full order lifecycle tracking.

---

## TECH STACK & SETUP

- **Framework:** React 18+ with TypeScript
- **Routing:** React Router v6
- **State Management:** Zustand or React Context
- **HTTP Client:** Axios (with `withCredentials: true` for session cookies)
- **UI Library:** Shadcn/ui + Tailwind CSS
- **Icons:** Lucide React
- **Charts:** Recharts (for dashboard stats)
- **Tables:** TanStack Table (for data grids with pagination, sorting, filtering)
- **Forms:** React Hook Form + Zod validation
- **Toasts/Notifications:** Sonner
- **Date handling:** date-fns
- **Base API URL:** `http://localhost:9090/api`

---

## COLOR PALETTE & DESIGN SYSTEM — VERY IMPORTANT

**DO NOT use green, blue, or teal anywhere in the UI.** The design must feel premium, warm, and bold.

### Primary Palette:
| Token             | Hex       | Usage                                       |
|-------------------|-----------|---------------------------------------------|
| `--primary`       | `#E63946` | Primary buttons, active nav, key CTAs        |
| `--primary-hover` | `#C1121F` | Primary button hover states                  |
| `--primary-light` | `#FFF0F1` | Primary subtle backgrounds, badges           |

### Secondary / Accent:
| Token              | Hex       | Usage                                      |
|--------------------|-----------|---------------------------------------------|
| `--accent`         | `#F4A261` | Accent highlights, warning badges, charts   |
| `--accent-hover`   | `#E38D3D` | Accent hover states                         |
| `--accent-light`   | `#FFF5EB` | Accent subtle backgrounds                   |

### Neutrals (Dark Mode-ready):
| Token               | Hex       | Usage                                     |
|----------------------|-----------|-------------------------------------------|
| `--bg-primary`       | `#0D0D0D` | Main background (dark mode)               |
| `--bg-secondary`     | `#1A1A1A` | Cards, sidebar, panels                    |
| `--bg-tertiary`      | `#262626` | Hover states, table row alt               |
| `--bg-light`         | `#FAFAFA` | Main background (light mode)              |
| `--bg-card-light`    | `#FFFFFF` | Cards in light mode                       |
| `--border`           | `#2E2E2E` | Borders in dark / `#E5E5E5` in light      |
| `--text-primary`     | `#F5F5F5` | Primary text dark / `#1A1A1A` light       |
| `--text-secondary`   | `#A3A3A3` | Secondary text / labels                   |
| `--text-muted`       | `#6B6B6B` | Muted / placeholder text                  |

### Semantic Colors (NO green, NO blue):
| Token             | Hex       | Usage                                       |
|-------------------|-----------|---------------------------------------------|
| `--success`       | `#D4A017` | Gold-ish success — confirmations, payments   |
| `--warning`       | `#F4A261` | Warnings, pending states                     |
| `--danger`        | `#E63946` | Errors, rejected, canceled                   |
| `--info`          | `#9B5DE5` | Info badges, tips, neutral statuses          |

### Loyalty Tier Colors:
| Tier       | Color     | Badge Style                              |
|------------|-----------|------------------------------------------|
| `BASIC`    | `#A3A3A3` | Gray, muted, subtle                      |
| `SILVER`   | `#C0C0C0` | Silver shimmer, light metallic           |
| `GOLD`     | `#D4A017` | Rich gold                                |
| `PLATINUM` | `#9B5DE5` | Deep purple, premium feel                |

### Order Status Colors:
| Status       | Color     | Style                                    |
|--------------|-----------|------------------------------------------|
| `PENDING`    | `#F4A261` | Amber/orange badge                       |
| `CONFIRMED`  | `#D4A017` | Gold badge                               |
| `CANCELED`   | `#6B6B6B` | Muted gray badge                         |
| `REJECTED`   | `#E63946` | Red badge                                |

### Payment Status Colors:
| Status       | Color     | Style                                    |
|--------------|-----------|------------------------------------------|
| `EN_ATTENTE` | `#F4A261` | Amber/orange                             |
| `ENCAISSE`   | `#D4A017` | Gold                                     |
| `REJETE`     | `#E63946` | Red                                      |

### Typography:
- **Font:** `Inter` (headings + body) or `Plus Jakarta Sans`
- **Headings:** Bold, tracking tight
- **Body:** Regular/Medium 14-16px
- **Monospace (IDs, references):** `JetBrains Mono` or `Fira Code`

### General Design Rules:
- Dark mode by default with light mode toggle
- Rounded corners (`radius: 8-12px`)
- Subtle shadows, no harsh drop shadows
- Smooth transitions (150-200ms)
- Glass-morphism effects on cards (subtle `backdrop-blur`)
- Micro-interactions on buttons and cards
- No gradients with green or blue tones anywhere

---

## AUTHENTICATION — HTTP Session Based

The backend uses **HTTP Session** (NOT JWT). The session cookie is set automatically by the server.

- **All Axios requests must include `withCredentials: true`**
- On app load, call `GET /api/auth/me` to check if a session exists
- If 401 → redirect to login page
- Store user info in a global auth store

### Session User Object (returned by login and /me):
```json
{
  "id": 1,
  "username": "admin",
  "role": "ADMIN",
  "clientId": null
}
```
For CLIENT role, `clientId` will be a number linking them to their client record.

---

## ROLE-BASED ACCESS

### Two Roles: `ADMIN` and `CLIENT`

**ADMIN** (MicroTech employee) — Full access:
- Dashboard with global stats
- Full CRUD on Clients, Products
- Create orders for any client
- Register payments
- Confirm / Cancel orders
- See all data

**CLIENT** (B2B customer company) — Read-only restricted:
- Can see ONLY their own profile
- Can see ONLY their own order history
- Can see product catalog (read-only)
- CANNOT create, edit, or delete anything
- CANNOT see other clients' data

**Implementation:**
- After login, check `role` from session
- Render different sidebar/navigation based on role
- Hide action buttons (create, edit, delete) for CLIENT role
- Redirect CLIENT away from admin-only routes

---

## COMPLETE API ENDPOINTS REFERENCE

### 1. Authentication (`/api/auth`)

#### POST `/api/auth/login`
Login and create session.
- **Request Body:**
```json
{
  "username": "admin",
  "password": "admin123"
}
```
- **Response 200:**
```json
{
  "id": 1,
  "username": "admin",
  "role": "ADMIN",
  "clientId": null
}
```

#### POST `/api/auth/logout`
Destroy session.
- **Response 200:** `"Logged out successfully"`

#### GET `/api/auth/me`
Get current logged-in user (session check).
- **Response 200:** Same as login response
- **Response 401:** No active session

---

### 2. Clients (`/api/clients`) — ADMIN only for write operations

#### POST `/api/clients`
Create a new client (also creates a User account for them).
- **Permission:** ADMIN only
- **Request Body:**
```json
{
  "nom": "TechCorp SARL",
  "email": "contact@techcorp.ma",
  "telephone": "0522-123456",
  "adresse": "123 Bd Zerktouni, Casablanca",
  "username": "techcorp",
  "password": "pass123"
}
```
- **Response 201:**
```json
{
  "id": 1,
  "nom": "TechCorp SARL",
  "email": "contact@techcorp.ma",
  "telephone": "0522-123456",
  "adresse": "123 Bd Zerktouni, Casablanca",
  "tier": "BASIC",
  "totalOrders": 0,
  "totalSpent": 0.0,
  "firstOrderDate": null,
  "lastOrderDate": null
}
```

#### GET `/api/clients`
Get all clients.
- **Permission:** ADMIN only
- **Response 200:** Array of ClientResponse objects

#### GET `/api/clients/{id}`
Get a specific client by ID.
- **Permission:** ADMIN or CLIENT (own record only)
- **Response 200:** ClientResponse object

#### PUT `/api/clients/{id}`
Update client information.
- **Permission:** ADMIN only
- **Request Body:** Same as create
- **Response 200:** Updated ClientResponse

#### DELETE `/api/clients/{id}`
Delete a client.
- **Permission:** ADMIN only
- **Response 204:** No content

#### GET `/api/clients/{id}/orders`
Get order history for a specific client.
- **Permission:** ADMIN or CLIENT (own orders only)
- **Response 200:** Array of OrderResponse objects

---

### 3. Products (`/api/products`)

#### POST `/api/products`
Create a new product.
- **Permission:** ADMIN only
- **Request Body:**
```json
{
  "nom": "HP ProBook 450 G10",
  "description": "Intel i5, 8GB RAM, 256GB SSD",
  "prixUnitaire": 8500.00,
  "stock": 50
}
```
- **Response 201:**
```json
{
  "id": 1,
  "nom": "HP ProBook 450 G10",
  "description": "Intel i5, 8GB RAM, 256GB SSD",
  "prixUnitaire": 8500.00,
  "stock": 50
}
```

#### GET `/api/products`
Get all products (paginated).
- **Permission:** Any authenticated user
- **Query Params:** `page` (default 0), `size` (default 10)
- **Response 200:** Spring Page object:
```json
{
  "content": [ ...ProductResponse array... ],
  "pageable": { "pageNumber": 0, "pageSize": 10 },
  "totalElements": 25,
  "totalPages": 3,
  "last": false,
  "first": true,
  "number": 0,
  "size": 10,
  "numberOfElements": 10,
  "empty": false
}
```

#### GET `/api/products/{id}`
Get a product by ID.
- **Permission:** Any authenticated user
- **Response 200:** ProductResponse

#### PUT `/api/products/{id}`
Update a product.
- **Permission:** ADMIN only
- **Request Body:** Same as create
- **Response 200:** Updated ProductResponse

#### DELETE `/api/products/{id}`
Soft-delete a product (if used in orders) or hard-delete.
- **Permission:** ADMIN only
- **Response 204:** No content

---

### 4. Orders (`/api/orders`)

#### POST `/api/orders`
Create a new order.
- **Permission:** ADMIN only
- **Request Body:**
```json
{
  "clientId": 1,
  "items": [
    { "productId": 1, "quantite": 2 },
    { "productId": 3, "quantite": 5 }
  ],
  "codePromo": "PROMO-AB12"
}
```
`codePromo` is optional. Must match pattern `PROMO-XXXX` (4 alphanumeric chars).
- **Response 201:**
```json
{
  "id": 1,
  "clientId": 1,
  "clientNom": "TechCorp SARL",
  "dateCreation": "2026-02-28T10:30:00",
  "sousTotal": 25000.00,
  "montantRemise": 1250.00,
  "montantHtApresRemise": 23750.00,
  "tva": 4750.00,
  "totalTtc": 28500.00,
  "montantRestant": 28500.00,
  "codePromo": "PROMO-AB12",
  "status": "PENDING",
  "items": [
    {
      "id": 1,
      "productId": 1,
      "productNom": "HP ProBook 450 G10",
      "quantite": 2,
      "prixUnitaire": 8500.00,
      "totalLigne": 17000.00
    },
    {
      "id": 2,
      "productId": 3,
      "productNom": "Dell Monitor 27\"",
      "quantite": 5,
      "prixUnitaire": 1600.00,
      "totalLigne": 8000.00
    }
  ]
}
```

#### GET `/api/orders`
Get all orders.
- **Permission:** ADMIN only
- **Response 200:** Array of OrderResponse

#### GET `/api/orders/{id}`
Get a specific order.
- **Permission:** ADMIN, or CLIENT (own orders only)
- **Response 200:** OrderResponse

#### PUT `/api/orders/{id}/confirm`
Confirm an order (only if fully paid — `montantRestant == 0`).
- **Permission:** ADMIN only
- **Response 200:** Updated OrderResponse with `status: "CONFIRMED"`

#### PUT `/api/orders/{id}/cancel`
Cancel an order (only if PENDING).
- **Permission:** ADMIN only
- **Response 200:** Updated OrderResponse with `status: "CANCELED"`

**Order Status Flow:**
```
PENDING → CONFIRMED  (admin, after full payment)
PENDING → CANCELED   (admin, manual)
PENDING → REJECTED   (system, if stock insufficient at creation)
CONFIRMED/CANCELED/REJECTED = final states (no further changes)
```

---

### 5. Payments (`/api/payments`)

#### POST `/api/payments`
Register a payment for an order.
- **Permission:** ADMIN only
- **Request Body:**
```json
{
  "orderId": 1,
  "montant": 6000.00,
  "typePaiement": "ESPECES",
  "reference": "RECU-001",
  "banque": null,
  "dateEcheance": null,
  "dateEncaissement": "2026-02-28",
  "status": "ENCAISSE"
}
```
- **Payment Methods:** `ESPECES`, `CHEQUE`, `VIREMENT`
  - `ESPECES`: Max 20,000 DH per payment. Immediate. Status = ENCAISSE.
  - `CHEQUE`: Requires reference, banque, dateEcheance. Can be deferred. Status = EN_ATTENTE / ENCAISSE / REJETE.
  - `VIREMENT`: Requires reference, banque. Can be deferred. Status = EN_ATTENTE / ENCAISSE / REJETE.
- **Payment statuses:** `EN_ATTENTE`, `ENCAISSE`, `REJETE`
- **Response 201:**
```json
{
  "id": 1,
  "orderId": 1,
  "numeroPaiement": 1,
  "montant": 6000.00,
  "typePaiement": "ESPECES",
  "datePaiement": "2026-02-28T14:00:00",
  "dateEncaissement": "2026-02-28",
  "status": "ENCAISSE",
  "reference": "RECU-001",
  "banque": null,
  "dateEcheance": null
}
```

#### PUT `/api/payments/{id}`
Update payment status (e.g., encash a pending check).
- **Permission:** ADMIN only
- **Request Body:**
```json
{
  "status": "ENCAISSE",
  "dateEncaissement": "2026-03-15"
}
```
- **Response 200:** Updated PaymentResponse

#### GET `/api/payments/order/{orderId}`
Get all payments for a specific order.
- **Permission:** Any authenticated user
- **Response 200:** Array of PaymentResponse

#### GET `/api/payments/{id}`
Get a single payment by ID.
- **Permission:** Any authenticated user
- **Response 200:** PaymentResponse

---

### Error Response Format (all errors):
```json
{
  "timestamp": "2026-02-28T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Client not found with id: 99",
  "path": "/api/clients/99"
}
```
HTTP codes used: `400` (validation), `401` (unauthenticated), `403` (forbidden), `404` (not found), `422` (business rule violation), `500` (internal error).

---

## APPLICATION PAGES & LAYOUT

### Global Layout:
- **Sidebar** (collapsible on mobile): Navigation links based on role
- **Top bar**: App logo "SmartShop", breadcrumbs, dark/light mode toggle, user dropdown (name, role, logout)
- **Main content area**: Pages render here

### Pages to Build:

#### 1. Login Page (`/login`)
- Clean, centered card with the SmartShop logo
- Username + Password fields
- "Sign In" button with loading state
- Error message display for wrong credentials
- Redirect to dashboard on success
- No registration — accounts are created by admin

#### 2. Dashboard (`/` or `/dashboard`) — ADMIN
- **KPI Cards row:**
  - Total Clients (count from GET /api/clients)
  - Total Orders (count from GET /api/orders)
  - Total Revenue (sum of totalTtc from confirmed orders)
  - Pending Orders (count of PENDING status)
- **Charts:**
  - Order status distribution (pie/donut chart: PENDING, CONFIRMED, CANCELED, REJECTED)
  - Top 5 clients by totalSpent (horizontal bar chart)
  - Recent orders table (last 5-10 orders)
- **Quick Actions:** Create Order, Add Client, Add Product

#### 3. Client Dashboard (`/my-dashboard`) — CLIENT
- Welcome message with client name and tier badge
- Stats cards: Total Orders, Total Spent, Loyalty Tier
- Recent orders list (their own)
- Tier progress visualization (show how close to next tier)

#### 4. Clients List (`/clients`) — ADMIN only
- Data table with columns: ID, Name, Email, Phone, Tier (colored badge), Total Orders, Total Spent
- Search/filter by name, email, tier
- "Add Client" button → opens modal/drawer form
- Row click → Client detail page
- Edit and Delete actions per row

#### 5. Client Detail (`/clients/:id`)
- Profile card: Name, email, phone, address, created date
- Loyalty tier badge (large, prominent, colored)
- Stats: Total Orders, Total Spent, First Order Date, Last Order Date
- **Tier Progress Bar:** Visual indicator showing progress toward next tier
  - Show both conditions: orders count AND total spent
  - Whichever reaches threshold first triggers upgrade
- Order history table (from GET /clients/{id}/orders):
  - Columns: Order ID, Date, Total TTC, Status (badge), Actions (view)

#### 6. Products List (`/products`)
- Data table with server-side pagination (page, size params)
- Columns: ID, Name, Description, Unit Price (formatted as DH), Stock (with low-stock warning if < 10)
- ADMIN: Add, Edit, Delete buttons
- CLIENT: Read-only view, no action buttons
- Search by product name
- Stock badge: red if 0, orange if < 10, normal otherwise

#### 7. Product Form (Modal or `/products/new`, `/products/:id/edit`) — ADMIN
- Fields: Name, Description, Unit Price (number), Stock (number)
- Validation matching backend constraints
- Save + Cancel buttons

#### 8. Orders List (`/orders`) — ADMIN: all orders, CLIENT: their own orders
- Data table columns: ID, Client Name, Date, Sous-Total, Remise, Total TTC, Remaining Amount, Status (colored badge)
- Filter by status (PENDING, CONFIRMED, CANCELED, REJECTED)
- Search by client name or order ID
- "Create Order" button (ADMIN only)
- Row click → Order detail

#### 9. Create Order (`/orders/new`) — ADMIN only
- **Step 1:** Select client (searchable dropdown from GET /api/clients)
- **Step 2:** Add products:
  - Searchable product selector
  - Quantity input per product
  - Show line total (price × quantity) live
  - Running subtotal display
  - Remove item button
- **Step 3:** Optional promo code input (format: PROMO-XXXX)
- **Live calculation preview:**
  - Sous-Total HT
  - Loyalty discount (show tier + percentage)
  - Promo discount (if code applied)
  - Total discount
  - HT après remise
  - TVA (20%)
  - **Total TTC** (large, bold)
- Submit button → POST /api/orders

#### 10. Order Detail (`/orders/:id`)
- **Order header:** ID, Date, Status badge, Client name (link to client)
- **Financial summary card:**
  - Sous-Total HT
  - Discount breakdown (loyalty + promo)
  - HT après remise
  - TVA amount
  - Total TTC
  - **Amount Remaining** (prominent, with progress bar showing % paid)
- **Order items table:** Product name, Quantity, Unit Price, Line Total
- **Payments section:**
  - List of all payments (from GET /payments/order/{orderId})
  - Each payment: #number, amount, method (icon+label), date, status badge, reference, bank
  - "Add Payment" button (ADMIN only, only if PENDING and montantRestant > 0)
  - **Payment progress bar** showing paid vs remaining
- **Admin Actions** (only if PENDING):
  - "Confirm Order" button (only enabled if montantRestant == 0) → PUT /orders/{id}/confirm
  - "Cancel Order" button → PUT /orders/{id}/cancel with confirmation dialog
- If status is CONFIRMED/CANCELED/REJECTED → show reason and disable all actions

#### 11. Add Payment Modal/Drawer — ADMIN only
- Fields:
  - Amount (number, max = montantRestant)
  - Payment Method (select: ESPECES, CHEQUE, VIREMENT)
  - **Dynamic fields based on method:**
    - ESPECES: Reference (receipt number). Note: max 20,000 DH enforced
    - CHEQUE: Reference (check number), Bank, Due Date (dateEcheance)
    - VIREMENT: Reference (transfer ref), Bank
  - Status (select: EN_ATTENTE, ENCAISSE, REJETE)
  - Encashment Date (dateEncaissement) — optional, for when actually cashed
- After successful payment → refresh order detail to show updated montantRestant

#### 12. Payments Overview (`/payments`) — ADMIN only (optional but recommended)
- Table of all payments across orders
- Columns: ID, Order ID, Payment #, Amount, Method, Date, Status, Reference, Bank
- Filter by status (EN_ATTENTE — useful for tracking pending checks/transfers)
- Quick action to update status (encash or reject)

---

## BUSINESS LOGIC TO REFLECT IN UI

### Loyalty Tier System:
Display tier acquisition rules prominently in client detail:
- **BASIC:** Default (0 orders)
- **SILVER:** 3+ orders OR 1,000+ DH cumulated
- **GOLD:** 10+ orders OR 5,000+ DH cumulated
- **PLATINUM:** 20+ orders OR 15,000+ DH cumulated

Tier discount applied on future orders:
- **SILVER:** 5% if order subtotal ≥ 500 DH
- **GOLD:** 10% if order subtotal ≥ 800 DH
- **PLATINUM:** 15% if order subtotal ≥ 1,200 DH

Show this info in the Create Order form when a client is selected.

### Promo Code:
- Format: `PROMO-XXXX` (4 uppercase alphanumeric characters)
- Gives an additional +5% discount
- Can stack with loyalty discount

### Order Calculation (shown live in Create Order):
```
Sous-Total HT = Σ(prix_unitaire × quantité)
Loyalty Discount = Sous-Total × tier_percentage (if subtotal meets minimum)
Promo Discount = Sous-Total × 5% (if valid code)
Total Discount = Loyalty + Promo
HT après remise = Sous-Total - Total Discount
TVA = HT après remise × 20%
Total TTC = HT après remise + TVA
```

### Payment Rules:
- A PENDING order can receive multiple payments with different methods
- ESPECES: max 20,000 DH per single payment
- Order can only be CONFIRMED when montantRestant = 0
- Payment amounts are in DH, 2 decimal places
- Show clear remaining amount after each payment

---

## UI/UX REQUIREMENTS

1. **Responsive:** Desktop-first but mobile-friendly (sidebar collapses to hamburger)
2. **Loading states:** Skeleton loaders for tables and cards
3. **Empty states:** Illustrated empty states with CTAs ("No clients yet — Add your first client")
4. **Confirmation dialogs:** For destructive actions (delete client, cancel order, reject payment)
5. **Toast notifications:** Success/error feedback for all actions
6. **Currency formatting:** All monetary values displayed as `XX,XXX.XX DH` (Moroccan Dirham)
7. **Date formatting:** Display dates in French locale format (`28 Feb 2026` or `28/02/2026`)
8. **Badge components:** Consistent colored badges for all statuses and tiers
9. **Breadcrumbs:** Show navigation path (Dashboard > Clients > TechCorp SARL)
10. **Search:** Debounced search inputs (300ms)
11. **Pagination:** Page size selector (10, 25, 50) + page navigation
12. **Form validation:** Client-side validation matching backend constraints before submission
13. **Optimistic updates:** Where appropriate (e.g., status changes)
14. **Error handling:** Global error interceptor for 401 (redirect to login), display error messages from API

---

## FILE STRUCTURE (suggested)

```
src/
├── api/
│   ├── axiosConfig.ts          # Axios instance with baseURL + withCredentials
│   ├── authApi.ts
│   ├── clientApi.ts
│   ├── productApi.ts
│   ├── orderApi.ts
│   └── paymentApi.ts
├── components/
│   ├── layout/
│   │   ├── Sidebar.tsx
│   │   ├── TopBar.tsx
│   │   └── AppLayout.tsx
│   ├── ui/                     # shadcn components
│   ├── shared/
│   │   ├── StatusBadge.tsx
│   │   ├── TierBadge.tsx
│   │   ├── CurrencyDisplay.tsx
│   │   ├── ConfirmDialog.tsx
│   │   ├── DataTable.tsx
│   │   ├── EmptyState.tsx
│   │   └── LoadingSkeleton.tsx
│   ├── clients/
│   │   ├── ClientForm.tsx
│   │   ├── ClientTable.tsx
│   │   └── TierProgress.tsx
│   ├── products/
│   │   ├── ProductForm.tsx
│   │   └── ProductTable.tsx
│   ├── orders/
│   │   ├── CreateOrderForm.tsx
│   │   ├── OrderTable.tsx
│   │   ├── OrderDetail.tsx
│   │   ├── OrderCalculation.tsx
│   │   └── PaymentProgress.tsx
│   └── payments/
│       ├── PaymentForm.tsx
│       ├── PaymentList.tsx
│       └── PaymentStatusUpdate.tsx
├── pages/
│   ├── LoginPage.tsx
│   ├── DashboardPage.tsx
│   ├── ClientDashboardPage.tsx
│   ├── ClientsPage.tsx
│   ├── ClientDetailPage.tsx
│   ├── ProductsPage.tsx
│   ├── OrdersPage.tsx
│   ├── OrderDetailPage.tsx
│   ├── CreateOrderPage.tsx
│   └── PaymentsPage.tsx
├── stores/
│   └── authStore.ts
├── types/
│   ├── auth.ts
│   ├── client.ts
│   ├── product.ts
│   ├── order.ts
│   ├── payment.ts
│   └── enums.ts
├── hooks/
│   ├── useAuth.ts
│   ├── useClients.ts
│   ├── useProducts.ts
│   ├── useOrders.ts
│   └── usePayments.ts
├── lib/
│   ├── utils.ts                # Currency formatting, date formatting
│   └── constants.ts            # Tier thresholds, colors
├── App.tsx
├── Router.tsx
└── main.tsx
```

---

## TYPESCRIPT TYPES TO DEFINE

```typescript
// enums.ts
export type UserRole = 'ADMIN' | 'CLIENT';
export type CustomerTier = 'BASIC' | 'SILVER' | 'GOLD' | 'PLATINUM';
export type OrderStatus = 'PENDING' | 'CONFIRMED' | 'CANCELED' | 'REJECTED';
export type PaymentMethod = 'ESPECES' | 'CHEQUE' | 'VIREMENT';
export type PaymentStatus = 'EN_ATTENTE' | 'ENCAISSE' | 'REJETE';

// auth.ts
export interface SessionUser {
  id: number;
  username: string;
  role: UserRole;
  clientId: number | null;
}

export interface LoginRequest {
  username: string;
  password: string;
}

// client.ts
export interface ClientRequest {
  nom: string;
  email: string;
  telephone: string;
  adresse?: string;
  username: string;
  password: string;
}

export interface ClientResponse {
  id: number;
  nom: string;
  email: string;
  telephone: string;
  adresse: string;
  tier: CustomerTier;
  totalOrders: number;
  totalSpent: number;
  firstOrderDate: string | null;
  lastOrderDate: string | null;
}

// product.ts
export interface ProductRequest {
  nom: string;
  description?: string;
  prixUnitaire: number;
  stock: number;
}

export interface ProductResponse {
  id: number;
  nom: string;
  description: string;
  prixUnitaire: number;
  stock: number;
}

export interface Page<T> {
  content: T[];
  pageable: { pageNumber: number; pageSize: number };
  totalElements: number;
  totalPages: number;
  last: boolean;
  first: boolean;
  number: number;
  size: number;
  numberOfElements: number;
  empty: boolean;
}

// order.ts
export interface OrderItemRequest {
  productId: number;
  quantite: number;
}

export interface OrderRequest {
  clientId: number;
  items: OrderItemRequest[];
  codePromo?: string;
}

export interface OrderItemResponse {
  id: number;
  productId: number;
  productNom: string;
  quantite: number;
  prixUnitaire: number;
  totalLigne: number;
}

export interface OrderResponse {
  id: number;
  clientId: number;
  clientNom: string;
  dateCreation: string;
  sousTotal: number;
  montantRemise: number;
  montantHtApresRemise: number;
  tva: number;
  totalTtc: number;
  montantRestant: number;
  codePromo: string | null;
  status: OrderStatus;
  items: OrderItemResponse[];
}

// payment.ts
export interface PaymentRequest {
  orderId: number;
  montant: number;
  typePaiement: PaymentMethod;
  status?: PaymentStatus;
  reference?: string;
  banque?: string;
  dateEcheance?: string;
  dateEncaissement?: string;
}

export interface UpdatePaymentStatusRequest {
  status: PaymentStatus;
  dateEncaissement?: string;
}

export interface PaymentResponse {
  id: number;
  orderId: number;
  numeroPaiement: number;
  montant: number;
  typePaiement: PaymentMethod;
  datePaiement: string;
  dateEncaissement: string | null;
  status: PaymentStatus;
  reference: string;
  banque: string | null;
  dateEcheance: string | null;
}

// error.ts
export interface ErrorResponse {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
}
```

---

## IMPORTANT IMPLEMENTATION NOTES

1. **Session Auth:** Set up Axios globally with `withCredentials: true`. The backend runs on `http://localhost:9090`. Handle CORS if needed on the backend side.
2. **No registration flow:** Only ADMIN can create client accounts via POST /api/clients (which also creates their User login).
3. **Currency:** All monetary values are in Moroccan Dirhams (DH). Format with 2 decimal places and thousands separator.
4. **Server port:** Backend runs on port `9090`.
5. **Pagination:** Only products endpoint uses server-side pagination. Others return full arrays.
6. **Soft delete on products:** Deleted products won't appear in the list but remain in existing order items. The UI doesn't need to handle this explicitly — just refresh the product list after delete.
7. **Order lifecycle:** Orders start as PENDING. They can only be confirmed after full payment. Show clear visual cues for what actions are available based on current status and payment state.
8. **Payment method icons:** Use distinct icons — banknotes for ESPECES, file-text for CHEQUE, arrow-right-left for VIREMENT.
9. **No JWT:** Don't implement any token-based auth. The browser session cookie handles everything automatically.

---

Generate ALL the code for this complete frontend application. Every component, every page, every API call, every type, fully styled with the color palette specified. The UI should feel like a premium SaaS dashboard — clean, professional, data-dense but not cluttered, with excellent use of whitespace, typography hierarchy, and the warm red/amber/gold/purple palette defined above. Absolutely NO green and NO blue anywhere.
