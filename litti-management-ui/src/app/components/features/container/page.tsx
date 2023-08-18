"use client";
import { Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import '@/app/globals.css'
import "@/app/styles/entity.css"
import { useRouter } from "next/navigation";
import EntityMenu, { OPTION_LIST } from "@/app/components/common/menu/page";
import { useState } from "react";
import ModelListComponent from "@/app/components/models/list/page";
import ModelAddComponent from "../add/page";
import FeatureListComponent from "../list/page";
import FeatureAddComponent from "../add/page";

// `app/dashboard/page.tsx` is the UI for the `/dashboard` URL
export default function FeatureContainerComponent() {
  const router = useRouter();
  let [selectedOption, setSelectedOption] = useState(OPTION_LIST);
  return <Row>
      <EntityMenu setSelectedOption={setSelectedOption} selectedOption={selectedOption} />
      {selectedOption == OPTION_LIST ? <FeatureListComponent/> : <FeatureAddComponent  setSelectedOption={setSelectedOption}/>}
    </Row>
}