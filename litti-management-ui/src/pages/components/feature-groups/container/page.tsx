"use client";
import { Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import '@/app/globals.css'
import "@/app/styles/entity.css"
import { useRouter } from "next/navigation";
import EntityMenu from "@/pages/components/common/menu/page";
import { useState } from "react";
import FeatureGroupsListComponent from "@/pages/components/feature-groups/list/page";
import FeatureGroupAddComponent from "@/pages/components/feature-groups/add/page";
import { OPTION_LIST } from "@/app/common/constants";

// `app/dashboard/page.tsx` is the UI for the `/dashboard` URL
export default function FeatureGroupContainerComponent() {
  const router = useRouter();
  let [selectedOption, setSelectedOption] = useState(OPTION_LIST);
  return <Row>
      <EntityMenu setSelectedOption={setSelectedOption} selectedOption={selectedOption} />
      {selectedOption == OPTION_LIST ? <FeatureGroupsListComponent/> : <FeatureGroupAddComponent  setSelectedOption={setSelectedOption}/>}
    </Row>
}