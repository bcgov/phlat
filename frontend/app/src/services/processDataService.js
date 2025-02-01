import { appAxios } from '~/services/interceptors';
import {
  cleanFilter,
  objectToQueryParams,
  transformSortBy,
} from '~/utils/filters';

export default {
  async serviceGetAllProcessData() {
    return appAxios().get(`processdata/view/all`);
  },
  async serviceGetProcessDataById(
    id,
    filter = {},
    { itemsPerPage, page, sortBy }
  ) {
    const paginationCriteria = objectToQueryParams({ itemsPerPage, page });
    return appAxios().post(
      `processdata/controltable/${id}?${paginationCriteria}`,
      { ...cleanFilter(filter), sort: transformSortBy(sortBy) }
    );
  },
  async serviceGetFormFieldsFromProcessData() {
    return appAxios().get(`processdata/column-display-names`);
  },
  async serviceGetSingleProcessDataByRecordId(id) {
    return appAxios().get(`processdata/view/${id}`);
  },
  async servicePutProcessDataById(id, data) {
    return appAxios().put(`processdata/update/${id}`, data);
  },
  async servicePutValidateAll(id) {
    return appAxios().put(`processdata/validateallbycontroltableid/${id}`);
  },
};
