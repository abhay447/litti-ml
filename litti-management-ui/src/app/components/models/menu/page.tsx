"use client";
import { Col, Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import '@/app/globals.css'
import Link from "next/link";
import '@/app/styles/entity.css';
// `app/dashboard/page.tsx` is the UI for the `/dashboard` URL

import { createContext, useState } from 'react';
import { useRouter, usePathname } from "next/navigation";
const OPTION_ADD = "add";
const OPTION_LIST = "list";
export default function ModelMenu() {

  const router = useRouter()
  const pathname = usePathname()
  const pathTokens = pathname.split("/")
  let [selectedOption, setSelectedOption] = useState(pathTokens[pathTokens.length - 1]);

  function handleOnClick(option: string) {
    setSelectedOption(option);
    router.push("/components/models/"+option)
  }

  function getOptionStyle(option: string) {
    if(option === selectedOption) {
      return "entity-top-menu-col entity-top-menu-col-selected";
    }
    return "entity-top-menu-col";
  }

  return (
    <Row>
      <Col style={{ display: 'flex', justifyContent: 'center' }} className={getOptionStyle(OPTION_LIST)}>
        <button type="button" onClick={() => handleOnClick(OPTION_LIST)}>List Models</button>
      </Col>
      <Col style={{ display: 'flex', justifyContent: 'center' }} className={getOptionStyle(OPTION_ADD)}>
        <button type="button" onClick={() => handleOnClick(OPTION_ADD)}>Add Model</button>
      </Col>
    </Row>
  )

}
