"use client";
import { Col, Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import '@/app/globals.css'
import Link from "next/link";
import '@/app/styles/entity.css';
// `app/dashboard/page.tsx` is the UI for the `/dashboard` URL
export default function ModelMenu() {
 
  return (
   <Row>
    <Col style={{display:'flex', justifyContent:'center'}} className="entity-top-menu-col"><Link href="/components/models/list">List Models</Link></Col>
    <Col style={{display:'flex', justifyContent:'center'}} className="entity-top-menu-col"><Link href="/components/models/add">Add Model</Link></Col>
  </Row>
  )

}