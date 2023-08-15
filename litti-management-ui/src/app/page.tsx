"use client"; // This is a client component 
import Link from 'next/link';
// add bootstrap css 

import 'bootstrap/dist/css/bootstrap.min.css';
export default function Home() {
  return <div>
    <li><Link href="/models/add">Add Model</Link></li>
    <li><Link href="/models/list">List Models</Link></li>
  </div>;
}
