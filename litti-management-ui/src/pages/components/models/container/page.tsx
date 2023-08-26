"use client";
import { Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import '@/app/globals.css'
import "@/app/styles/entity.css"

import EntityMenu from "@/pages/components/common/menu/page";
import { useState } from "react";
import ModelListComponent from "@/pages/components/models/list/page";
import ModelAddComponent from "../add/page";
import { OPTION_LIST } from "@/app/common/constants";

// `app/dashboard/page.tsx` is the UI for the `/dashboard` URL
export default function ModelContainerComponent() {

  let [selectedOption, setSelectedOption] = useState(OPTION_LIST);
  return <Row>
      <EntityMenu setSelectedOption={setSelectedOption} selectedOption={selectedOption} />
      {selectedOption == OPTION_LIST ? <ModelListComponent/> : <ModelAddComponent  setSelectedOption={setSelectedOption}/>}
    </Row>
}