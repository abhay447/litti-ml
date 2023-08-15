"use client"; // This is a client component 
import Link from 'next/link';
// add bootstrap css 

import 'bootstrap/dist/css/bootstrap.min.css';
import { Col, Container, Row } from 'reactstrap';
export default function Home() {
  return <Container>
    <Row style={{display:'flex', justifyContent:'center'}}><Link href="/models/add">Add Model</Link></Row>
    <Row style={{display:'flex', justifyContent:'center'}}><Link href="/models/list">List Models</Link></Row>
  </Container>;
}
