"use client";
import { Col, Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import '../../globals.css'
import Link from "next/link";

// `app/dashboard/page.tsx` is the UI for the `/dashboard` URL
export default function ModelMenu() {
 
  return (
   <Row>
    <Col style={{display:'flex', justifyContent:'left'}}><Link href="/models/list">List Models</Link></Col>
    <Col style={{display:'flex', justifyContent:'left'}}><Link href="/models/add">Add Model</Link></Col>
  </Row>
  )

}