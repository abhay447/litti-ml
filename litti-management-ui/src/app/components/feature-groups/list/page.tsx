"use client";
import { FeatureGroupEntity } from "@/app/entities/featureGroupEntity";
import { useEffect, useState } from "react"
import { Col, Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import '@/app/globals.css'
import {getFeatureGroups} from '@/app/services/featureGroupService';
import { DataGrid } from "@mui/x-data-grid";


const featureGroupTableColumns = [
  { field: 'name', headerName: 'Name' , width:150},
  { field: 'dimensions', headerName: 'dimensions' , width:300},
]

// `app/dashboard/page.tsx` is the UI for the `/dashboard` URL
export default function FeatureGroupsListComponent() {

  const [featureGroups, setFeatureGroups] = useState<Array<FeatureGroupEntity>>([])
  const [isLoading, setLoading] = useState(true)
 
  useEffect(() => {

    getFeatureGroups()
      .then((data) => {
        setFeatureGroups(data)
        setLoading(false)
      })
  }, []);
 
  if (isLoading) return <p>Loading...</p>
  if (featureGroups.length == 0) return <p>No profile data</p>
  
  return (
    <DataGrid rows={featureGroups} columns={featureGroupTableColumns}/>
  )

}