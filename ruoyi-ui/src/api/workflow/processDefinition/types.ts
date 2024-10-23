import type { BaseEntity, BasePageQuery } from '@/api/model/resultModel';

export interface ProcessDefinitionQuery extends BasePageQuery {
  key?: string;
  name?: string;
  categoryCode?: string;
}

export interface ProcessDefinitionVo extends BaseEntity {
  id: string;
  name: string;
  key: string;
  version: number;
  suspensionState: number;
  resourceName: string;
  diagramResourceName: string;
  deploymentId: string;
  deploymentTime: string;
}

export interface ProcessDefinitionXmlVO {
  xml: string[];
  xmlStr: string;
}
