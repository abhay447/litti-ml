"use client";
import { FeatureEntity } from "@/app/entities/featureEntity";
import { useEffect, useState } from "react"
import { Col, Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import '@/app/globals.css'
import { getFeatureGroupsMap } from "@/app/services/featureGroupService";
import { getFeatures } from "@/app/services/featureService";
import { FeatureGroupEntity } from "@/app/entities/featureGroupEntity";
import { DataGrid } from "@mui/x-data-grid";

const featureTableColumns = [
  { field: 'name', headerName: 'Name' , width:150},
  { field: 'version', headerName: 'Version' , width:150},
  { field: 'dataType', headerName: 'Data Type' , width:150},
  { field: 'defaultValue', headerName: 'Default Value' , width:150},
  { field: 'featureGroupName', headerName: 'Feature Group' , width:150},
  { field: 'ttlSeconds', headerName: 'Ttl Seconds' , width:150},
]

// `app/dashboard/page.tsx` is the UI for the `/dashboard` URL
export default function FeatureListComponent() {

  const [data, setData] = useState<FeatureEntity[]>()
  const [isLoading, setLoading] = useState(true)
  const [featureGroupsMap, setFeatureGroupsMap] = useState<Map<string, any>>()
  useEffect(() => {
    Promise.all([
      getFeatures(),
      getFeatureGroupsMap()
    ]).then(([features, featureGroupsMap]) => {
      setData(features);
      setFeatureGroupsMap(featureGroupsMap);
      setLoading(false);
    });
  }, [])
 
  if (isLoading) return <p>Loading...</p>
  if (!data) return <p>No profile data</p>

  const features = (data as Array<any>).map(
    entry => new FeatureEntity(
      entry.name,
      entry.version,
      entry.dataType,
      entry.defaultValue,
      entry.featureGroupId,
      entry.ttlSeconds,
      entry.id,
      featureGroupsMap?.get(entry.featureGroupId).name
    )
  )
 
  return (
        <DataGrid rows={features} columns={featureTableColumns}/>
  )
}