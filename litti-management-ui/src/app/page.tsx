"use client"; // This is a client component 
import Link from 'next/link';
// add bootstrap css 

import 'bootstrap/dist/css/bootstrap.min.css';
import { Col, Row } from 'reactstrap';
import RootLayout from '@/app/layout';
import { redirect } from 'next/navigation';
import { AppProps } from 'next/app';

function Home() {
    redirect('/models/list');
}