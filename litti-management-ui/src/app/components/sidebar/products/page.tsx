"use client";
import { Container, Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import '@/app/styles/sidebar.css'

// `app/dashboard/page.tsx` is the UI for the `/dashboard` URL
export default function SidebarProducts() { 
  return (
    
    <Container className="sidebar vh-100 d-flex flex-column">
      <Row className="sidebar-app-title">
        Litti Management
      </Row>
      <Row>
        Models
      </Row>
      <Row>
        Features
      </Row>
      <Row>
        Model Clusters
      </Row>
      <Row>
        Feature Stores
      </Row>
    </Container>
  )

}