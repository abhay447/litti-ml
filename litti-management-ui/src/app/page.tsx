"use client"; // This is a client component 
import Link from 'next/link';
// add bootstrap css 

import 'bootstrap/dist/css/bootstrap.min.css';
import { Col, Row } from 'reactstrap';
export default function Home() {
  return <Row>
    <Col style={{display:'flex', justifyContent:'center'}}><Link href="/models/add">Add Model</Link></Col>
    <Col style={{display:'flex', justifyContent:'center'}}><Link href="/models/list">List Models</Link></Col>
  </Row>;
}
