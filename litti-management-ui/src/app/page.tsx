"use client"; // This is a client component 
import Link from 'next/link';
// add bootstrap css 

import 'bootstrap/dist/css/bootstrap.min.css';
import { Col, Row } from 'reactstrap';
import RootContainerLayout from './components/container/layout';
import { redirect } from 'next/navigation';

export default function Home() {
    redirect('/models/list');
}
