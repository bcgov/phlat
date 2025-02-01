<script>
import BaseFilter from '../../components/base/BaseFilter.vue';
import BaseColumnFilter from '../../components/base/BaseColumnFilter.vue';
import BaseEditRecord from '../../components/base/BaseEditRecord.vue';
import BasePrompt from '../../components/base/BasePrompt.vue';
import BaseReportSummary from '../../components/base/BaseReportSummary.vue';
import { mapActions, mapState } from 'pinia';
import { useProcessDataStore } from '~/store/processData';
import { useControlTableDataStore } from '~/store/controltabledata';
import { useFilterDataStore } from '~/store/filtersDataStore';
import { useNotificationStore } from '~/store/notification';
import { useStatusDataStore } from '~/store/statusdata';
import { usePreferenceDataStore } from '~/store/displayColumnsPreference';
import { ViewNames, RowStatusCode, ControlStatusCode } from '~/utils/constants';
import BaseChips from '../../components/base/BaseChips.vue';
import _ from 'lodash';

export default {
  components: {
    BaseFilter,
    BaseEditRecord,
    BaseChips,
    BaseReportSummary,
    BasePrompt,
    BaseColumnFilter,
  },
  props: {
    id: {
      type: String,
      required: true,
    },
  },
  data: () => ({
    fileName: 'Loading...',
    countdownupdate: { count: 0, selectedId: null },
    dialog: false,
    isHovering: false,
    search: null,
    dialogDelete: false,
    showColumnsDialog: false,
    deleteSingleItem: {},
    filterData: {},
    pagination: {
      page: 1,
      itemsPerPage: 10,
      sortBy: 'id',
      sortDirection: 'asc',
    },
    filterIgnore: [],
    filterIgnoreColumns: [
      {
        key: 'id',
      },
      {
        key: 'controlTableId',
      },
      {
        key: 'actions',
      },
      {
        key: 'rowstatusCode',
      },
    ],
    ignoreToEdit: [
      { key: 'id' },
      { key: 'createdBy' },
      { key: 'createdAt' },
      { key: 'updatedAt' },
      { key: 'updatedBy' },
      { key: 'userId' },
      { key: 'statusCode' },
      { key: 'controlTableId' },
      { key: 'rowstatusCode' },
      { key: 'messages' },
      { key: 'doNotLoad' },
      { key: 'doNotLoadFlag' },
      { key: 'stakeholderId' },
      { key: 'hdsPauthId' },
      { key: 'hdsCategoryCode' },
      { key: 'hdsRoleTypeCode' },
      { key: 'hdsUserChid' },
      { key: 'hdsCreatedDts' },
      { key: 'hdsInvalidatedDts' },
      { key: 'hdsEffectiveEndDate' },
      { key: 'facCivicAddrId' },
      { key: 'facStreetDirection' },
      { key: 'streetDirectionPrefix' },
      { key: 'streetTypePrefix' },
      { key: 'facCivicNumber' },
      { key: 'facStreetName' },
      { key: 'facStreetType' },
      { key: 'facLocalityName' },
      { key: 'facProvinceCode' },
      { key: 'facDatabcResults' },
      { key: 'facTypeCode' },
    ],
    headers: [
      {
        title: 'Actions',
        key: 'actions',
        sortable: false,
        align: 'end',
      },
    ],
    onlyShowColumns: [],
    loading: true,
    inputSrcData: [],
    editedIndex: -1,
    editedItem: {},
    defaultItem: {},
    editStatusItem: {},
    editStatusNewItem: '',
    sortOrder: 'default',
    sortOrderCriteria: [], //{ key: 'id', order: 'asc' }
    sortOrderTypes: [
      {
        text: 'Row ID#',
        value: 'default',
        criteria: [{ key: 'id', order: 'asc' }],
      },
      {
        text: 'Fac Civic Address + HDS Name',
        value: 'facCivicAddressPlusHDSName',
        criteria: [
          { key: 'hdsName', order: 'asc' },
          { key: 'facCivicAddr', order: 'asc' },
        ],
      },
      {
        text: 'HDS Name (only)',
        value: 'HDSNameOnly',
        criteria: [{ key: 'hdsName', order: 'asc' }],
      },
    ],
    reportSummaryId: null,
    reportSummaryDialog: false,
    showValidateAllDialog: false,
  }),

  computed: {
    ...mapState(useProcessDataStore, [
      'processData',
      'totalItems',
      'formFieldHeaders',
      'deleteProcessDataById',
      'updatedProcessData',
      'validateAllStatus',
      'processingProcessData',
      'singleProcessDataByID',
    ]),
    ...mapState(useFilterDataStore, ['editSourceSelectedFiltersData']),
    ...mapState(usePreferenceDataStore, ['displayColumnsPreferenceData']),
    ...mapState(useStatusDataStore, ['allStatusData']),
    ...mapState(useControlTableDataStore, ['singleControlTableData']),
    formTitle() {
      return this.editedIndex === -1 ? 'New Item' : 'Edit Item';
    },
    IS_CONTROL_STATUS_APPROVED_OR_PREVALIDATED() {
      return (
        this.singleControlTableData.statusCode ===
          ControlStatusCode.PREVALIDATIONCOMPLETED ||
        this.singleControlTableData.statusCode === ControlStatusCode.APPROVED
      );
    },
    BASE_FILTER_HEADERS() {
      let headers = this.BASE_HEADERS.filter(
        (h) => !this.filterIgnore.some((fd) => fd.key === h.key)
      );
      return headers;
    },
    BASE_FILTER_HEADERS_FOR_MANAGE_COLUMNS() {
      let headers = this.BASE_FILTER_HEADERS.filter(
        (h) => !this.filterIgnoreColumns.some((fd) => fd.key === h.key)
      );
      return headers;
    },
    RowStatusCode: () => RowStatusCode,
    BASE_HEADERS() {
      let headers = [...this.headers];

      return headers;
    },
    HEADERS() {
      let headers;
      headers = this.BASE_HEADERS.filter(
        (h) => !this.filterIgnore.some((fd) => fd.key === h.key)
      );
      if (this.onlyShowColumns.length) {
        headers = headers.filter((h) =>
          this.onlyShowColumns.some((fd) => fd === h.key)
        );
      }

      const order = ['id', 'actions', 'rowstatusCode', 'messages'];

      headers = _.sortBy(headers, function (o) {
        let index = order.indexOf(o.key);
        return index === -1 ? Infinity : index;
      });

      return headers;
    },
    PRESELECTED_DATA() {
      return this.HEADERS.filter(
        (h) => !this.filterIgnore.some((fd) => fd.key === h.key)
      );
    },
    RESET_HEADERS() {
      let headers = this.BASE_FILTER_HEADERS;

      return headers;
    },
  },

  watch: {
    dialog(val) {
      val || this.close();
    },
    dialogDelete(val) {
      val || this.closeDelete();
    },
    processingProcessData(isLoading) {
      this.loading = isLoading;
    },
    processignPreferenceData(isLoading) {
      this.loading = isLoading;
    },
    editSourceSelectedFiltersData: {
      async handler() {
        await this.populateInputSource();
      },
      deep: true,
    },
  },

  async mounted() {
    this.initialize();
  },
  methods: {
    ...mapActions(useFilterDataStore, [
      'updateSelectedFiltersData',
      'clearAllFilters',
    ]),
    ...mapActions(useNotificationStore, ['addNotification']),
    ...mapActions(useProcessDataStore, [
      'fetchProcessDataByControlId',
      'fetchFormFieldHeaders',
      'updateSingleProcessRecord',
      'updateValidateAll',
      'fetchProcessDataByRecordId',
    ]),
    ...mapActions(usePreferenceDataStore, [
      'updateUserColumnsDisplayPreference',
      'fetchUserPreference',
      'processignPreferenceData',
    ]),
    ...mapActions(useControlTableDataStore, ['fetchGetControlTableById']),
    initialize() {
      this.loading = true;
      // this.populateInputSource();
      this.populateHeaders();
      this.populateControlTable();
      // this.populateStatus();
      this.loading = false;
    },
    clearFilters() {
      // this.updateSelectedFiltersData({}, 'editSrcData');
      this.clearAllFilters();
    },
    refreshControlTableList() {
      this.loading = true;
      this.populateInputSource();
      this.loading = false;
    },
    fetchRowStatusCodesAvailableToSwitch(thiseditStatusNewItem) {
      switch (thiseditStatusNewItem) {
        case RowStatusCode.INITIAL:
          return [RowStatusCode.DO_NOT_LOAD, RowStatusCode.ON_HOLD];
        case RowStatusCode.ON_HOLD:
          return [RowStatusCode.DO_NOT_LOAD, RowStatusCode.INITIAL];
        case RowStatusCode.DO_NOT_LOAD:
          return [RowStatusCode.ON_HOLD, RowStatusCode.INITIAL];
        case RowStatusCode.VALID:
          return [RowStatusCode.DO_NOT_LOAD, RowStatusCode.ON_HOLD];
        case RowStatusCode.INVALID:
          return [RowStatusCode.DO_NOT_LOAD, RowStatusCode.ON_HOLD];
        case RowStatusCode.WARNING:
          return [
            RowStatusCode.VALID,
            RowStatusCode.DO_NOT_LOAD,
            RowStatusCode.ON_HOLD,
            RowStatusCode.POTENTIAL_DUPLICATE,
          ];
        case RowStatusCode.POTENTIAL_DUPLICATE:
          return [
            RowStatusCode.VALID,
            RowStatusCode.ON_HOLD,
            RowStatusCode.DO_NOT_LOAD,
          ];
        case RowStatusCode.LOADERROR:
          return [RowStatusCode.DO_NOT_LOAD, RowStatusCode.ON_HOLD];
        default:
          return [];
      }
    },
    havingIssueOrWarning(data) {
      if (!Array.isArray(data)) {
        return false;
      }
      return data.some(
        (item) => item.messageType === 'WARNING' || item.messageType === 'ERROR'
      );
    },
    canRecordBeEdited(rowStatusCode) {
      return (
        this.IS_CONTROL_STATUS_APPROVED_OR_PREVALIDATED &&
        rowStatusCode !== RowStatusCode.COMPLETED
      );
    },
    async populateControlTable() {
      await this.fetchGetControlTableById(this.id);
      const controlTableData = this.singleControlTableData;
      this.fileName = controlTableData.fileName;
    },
    async populateHeaders() {
      // Get the header for this table
      await this.fetchFormFieldHeaders();
      // Get the headers from user preferences
      await this.fetchUserPreference(ViewNames.PROCESSVIEW);
      if (this.displayColumnsPreferenceData.length) {
        this.onlyShowColumns = this.displayColumnsPreferenceData;
      }

      const tableHeaders = this.formFieldHeaders.filter(
        /**
         * Removing this headers from the list just as a requirement from business for now,
         * on a later stage this should be removed by backend BCMOHAD-23110/BCMOHAD-23454
         */
        (header) => header !== 'doNotLoadFlag' && header !== 'doNotLoad'
      );

      this.headers = [...tableHeaders, ...this.headers].filter(
        ({ key }) => key !== 'controlTableId'
      );
    },
    loadItems(pageData) {
      this.pagination = pageData;
      this.populateInputSource();
    },
    async populateInputSource() {
      await this.fetchProcessDataByControlId(
        this.id,
        this.editSourceSelectedFiltersData,
        this.pagination
      );
      this.inputSrcData = this.processData;
    },

    async sortOrderHandle() {
      this.loading = true;
      const criteria = this.sortOrderTypes.find(
        (s) => s.value === this.sortOrder
      ).criteria;
      this.sortOrderCriteria = criteria;
      this.loading = false;
    },

    async requestValidateAll() {
      this.loading = true;
      await this.updateValidateAll(this.id);
      if (
        this.validateAllStatus &&
        this.validateAllStatus.status === 'success'
      ) {
        this.$router.push({
          name: 'TaskManagement',
        });
      }
      this.loading = true;
    },
    validateAll() {
      this.requestValidateAll();
    },
    onShowColumnDialog() {
      this.BASE_FILTER_HEADERS.sort(
        (a, b) =>
          this.PRESELECTED_DATA.findIndex((x) => x.title === b.title) -
          this.PRESELECTED_DATA.findIndex((x) => x.title === a.title)
      );

      this.showColumnsDialog = true;
    },

    async updateFilter(data, changeDisplayColumnsPreference = true) {
      this.loading = true;
      this.filterData = data;
      let preferences = {
        columns: [],
      };
      data.forEach((d) => {
        preferences.columns.push(d.key);
      });
      this.onlyShowColumns = preferences.columns;
      changeDisplayColumnsPreference &&
        (await this.updateUserColumnsDisplayPreference(
          ViewNames.PROCESSVIEW,
          preferences.columns
        ));
      this.showColumnsDialog = false;
      this.loading = false;
      // await this.populateInputSource();
    },

    editItem(item) {
      this.editedIndex = this.inputSrcData.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },

    editStatus(item) {
      this.editStatusItem = item.raw;
      this.editStatusNewItem = item.raw.rowstatusCode;
    },
    async saveNewStatus() {
      this.loading = true;
      try {
        await this.updateSingleProcessRecord(this.editStatusItem.id, {
          rowstatusCode: this.editStatusNewItem,
        });
        const data = this.updatedProcessData;

        if (data.id) {
          const i = this.inputSrcData.findIndex(
            (x) => x.id === this.editStatusItem.id
          );
          this.inputSrcData[i] = data;
        } else {
          this.addNotification({
            text: data.message || 'Something went wrong.',
            type: 'error',
          });
        }
      } catch (error) {
        this.addNotification({
          text: error.message || 'Something went wrong',
          type: 'error',
        });
      }

      this.loading = false;
      this.close();
      this.editStatusItem = {};
      this.editStatusNewItem = '';
    },
    closeEditStatus() {
      this.editStatusItem = {};
      this.editStatusNewItem = '';
    },
    close() {
      this.deleteSingleItem = {};
      this.editedItem = {};
      this.dialog = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },

    closeDelete() {
      this.deleteSingleItem = {};
      this.dialogDelete = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },

    save() {
      if (this.editedIndex > -1) {
        Object.assign(this.inputSrcData[this.editedIndex], this.editedItem);
      } else {
        this.inputSrcData.push(this.editedItem);
      }
      this.close();
    },
    async getProcessDataByRecordId(id) {
      try {
        await this.fetchProcessDataByRecordId(id);
        const data = this.singleProcessDataByID;
        if (data.id) {
          const i = this.inputSrcData.findIndex((x) => x.id === id);
          this.inputSrcData[i] = data;
        } else {
          this.addNotification({
            text: data.message || 'Something went wrong.',
            type: 'error',
          });
        }
      } catch (error) {
        this.addNotification({
          text: error.message || 'Something went wrong',
          type: 'error',
        });
      }
    },
    async handleRecordSave(selectedItemToEdit) {
      this.loading = true;
      try {
        await this.updateSingleProcessRecord(
          selectedItemToEdit.id,
          selectedItemToEdit
        );
        const data = this.updatedProcessData;
        if (data.id) {
          const i = this.inputSrcData.findIndex(
            (x) => x.id === selectedItemToEdit.id
          );
          data.rowstatusCode = 'Processing...';
          this.inputSrcData[i] = data;

          this.loading = false;
          this.close();
          this.countdownupdate.count = 10;
          this.countdownupdate.selectedId = selectedItemToEdit.id;
          const countdownInterval = setInterval(() => {
            this.countdownupdate.count -= 1;
            if (this.countdownupdate.count <= 0) {
              clearInterval(countdownInterval);
              this.countdownupdate.selectedId = null;
              this.getProcessDataByRecordId(selectedItemToEdit.id);
            }
          }, 1000);
        } else {
          this.addNotification({
            text: data.message || 'Something went wrong.',
            type: 'error',
          });
        }
      } catch (error) {
        this.addNotification({
          text: error.message || 'Something went wrong',
          type: 'error',
        });
      }
    },
    parseMessages(errorMsg) {
      if (!errorMsg) {
        return [];
      }
      try {
        return errorMsg;
      } catch (error) {
        return [];
      }
    },
    viewReportSummary(id) {
      this.reportSummaryId = id;
      this.reportSummaryDialog = true;
    },
    closeViewReportSummary() {
      this.reportSummaryId = null;
      this.reportSummaryDialog = false;
    },
  },
};
</script>
<template>
  <div>
    <div class="mt-6 d-flex flex-nowrap justify-content-sp-bt">
      <!-- page title -->
      <div class="page-title mw-50p">
        <h1>{{ fileName }} - Edit Source Data</h1>
      </div>

      <!-- search input -->
      <!-- <v-text-field
        v-model="search"
        density="compact"
        variant="underlined"
        label="Search"
        append-inner-icon="mdi-magnify"
        single-line
        solid
        class="header-component"
      ></v-text-field> -->
      <v-select
        v-model="sortOrder"
        :items="sortOrderTypes"
        label="Sort orders"
        item-title="text"
        density="compact"
        solid
        variant="underlined"
        class="header-component"
        @update:modelValue="sortOrderHandle"
      ></v-select>
      <div class="header-component">
        <span>
          <v-tooltip location="bottom">
            <template #activator="{ props }">
              <v-btn
                class="mx-1"
                color="primary"
                v-bind="props"
                size="x-small"
                density="default"
                icon="mdi:mdi-filter-remove"
                @click="clearFilters"
              />
            </template>
            <span>Clear Filter</span>
          </v-tooltip>
        </span>
        <span>
          <v-tooltip location="bottom">
            <template #activator="{ props }">
              <v-btn
                class="mx-1"
                color="primary"
                v-bind="props"
                size="x-small"
                density="default"
                icon="mdi:mdi-refresh"
                @click="refreshControlTableList"
              />
            </template>
            <span>Refresh list</span>
          </v-tooltip>
        </span>
        <span>
          <v-tooltip location="bottom">
            <template #activator="{ props }">
              <v-btn
                class="mx-1"
                color="primary"
                v-bind="props"
                size="x-small"
                density="default"
                icon="mdi:mdi-view-column"
                @click="onShowColumnDialog"
              />
            </template>
            <span>Manage Columns</span>
          </v-tooltip>
        </span>
        <span>
          <v-tooltip location="bottom">
            <template #activator="{ props }">
              <v-btn
                class="mx-1"
                color="primary"
                v-bind="props"
                size="x-small"
                density="default"
                icon="mdi:mdi-shield-account-variant-outline"
                @click="showValidateAllDialog = true"
              />
            </template>
            <span>Validate All</span>
          </v-tooltip>
        </span>
        <span>
          <!-- Summary Report -->
          <v-tooltip location="bottom">
            <template #activator="{ props }">
              <v-btn
                class="mx-1"
                color="primary"
                v-bind="props"
                size="x-small"
                density="default"
                icon="mdi:mdi-list-status"
                @click="viewReportSummary(id)"
              />
            </template>
            <span>View report summary</span>
          </v-tooltip>
        </span>
      </div>
    </div>

    <div>
      <div></div>
      <v-data-table-server
        key="forceTableRefresh"
        :loading="loading"
        height="70vh"
        :headers="HEADERS"
        fixed-header
        :items="inputSrcData"
        :items-length="totalItems"
        :items-per-page-options="[
          { value: 10, title: '10' },
          { value: 25, title: '25' },
          { value: 50, title: '50' },
          { value: 100, title: '100' },
          { value: totalItems, title: 'All' },
        ]"
        density="compact"
        :search="search"
        :sort-by="sortOrderCriteria"
        class="submissions-table"
        no-data-text="No data found"
        item-key="id"
        :multi-sort="true"
        @update:options="loadItems"
      >
        <template #headers="{ columns, isSorted, getSortIcon, toggleSort }">
          <tr>
            <template v-for="column in columns" :key="column.key">
              <th class="">
                <div class="v-data-table-header__content cursor-pointer">
                  <span
                    class="mr-2"
                    @click="
                      () => {
                        column.sortable === true && toggleSort(column);
                      }
                    "
                    >{{ column.title }}
                  </span>
                  <template v-if="isSorted(column) && column.sortable === true">
                    <v-icon
                      :icon="getSortIcon(column)"
                      @click="
                        () => {
                          column.sortable === true && toggleSort(column);
                        }
                      "
                    ></v-icon>
                  </template>
                  <BaseColumnFilter
                    v-if="column.filterable"
                    source-type="editSrcData"
                    :control-id="id"
                    :column="column"
                  />
                </div>
              </th>
            </template>
          </tr>
        </template>
        <template #item.rowstatusCode="{ item }">
          <div
            v-if="editStatusItem.id === item.raw.id"
            class="d-flex align-center"
          >
            <v-select
              v-model="editStatusNewItem"
              :items="
                fetchRowStatusCodesAvailableToSwitch(item.raw.rowstatusCode)
              "
              label="Status"
              density="compact"
              solid
              variant="outlined"
              class="me-2 width-max-content"
              :hide-details="true"
            ></v-select>
            <div class="d-flex flex-column justify-content-center">
              <v-tooltip location="right">
                <template #activator="{ props }">
                  <v-icon
                    v-bind="props"
                    size="small"
                    v-on="on"
                    @click="saveNewStatus()"
                  >
                    mdi-floppy
                  </v-icon>
                </template>
                <span>Save</span>
              </v-tooltip>
              <v-tooltip location="right">
                <template #activator="{ props }">
                  <v-icon
                    v-bind="props"
                    size="small"
                    v-on="on"
                    @click="closeEditStatus()"
                  >
                    mdi-close
                  </v-icon>
                </template>
                <span>Cancel</span>
              </v-tooltip>
            </div>
          </div>

          <div
            v-else
            class="d-flex align-center"
            @mouseenter="isHovering = item.raw.id"
            @mouseleave="isHovering = false"
          >
            <span class="d-flex align-center">
              {{ item.raw.rowstatusCode }}
              <div
                v-if="
                  countdownupdate.count > 0 &&
                  countdownupdate.selectedId === item.raw.id
                "
              >
                (Wait {{ countdownupdate.count }}s)
              </div>
            </span>
            <v-tooltip
              v-if="
                isHovering === item.raw.id &&
                fetchRowStatusCodesAvailableToSwitch(item.raw.rowstatusCode)
                  .length
              "
              location="right"
              :open-on-hover="isHovering === item.raw.id"
            >
              <template #activator="{ on }">
                <v-icon
                  size="small"
                  class="me-2"
                  v-on="on"
                  @click="editStatus(item)"
                >
                  mdi-pencil
                </v-icon>
              </template>
              <span>Update Status</span>
            </v-tooltip>
          </div>
        </template>
        <template #item.messages="{ item }">
          <BaseChips :messages="parseMessages(item.raw.messages)" />
        </template>
        <template #item.actions="{ item }">
          <v-tooltip
            v-if="canRecordBeEdited(item.raw.rowstatusCode)"
            location="bottom"
          >
            <template #activator="{ props }">
              <v-icon
                v-bind="props"
                size="small"
                class="me-2"
                @click="editItem(item)"
              >
                mdi-pencil
              </v-icon>
            </template>
            <span>Edit Record</span>
          </v-tooltip>
        </template>
        <template #no-data>
          <v-btn color="primary" @click="initialize"> Reset </v-btn>
        </template>
      </v-data-table-server>
      <v-dialog v-model="showValidateAllDialog" width="700">
        <BasePrompt
          prompt-body-text="Are you sure you want to validate all records?"
          :loading="loading"
          @do-action="validateAll"
          @abort-action="showValidateAllDialog = false"
        />
      </v-dialog>
      <v-dialog v-model="showColumnsDialog" width="700">
        <BaseFilter
          :loading="loading"
          input-filter-placeholder="Search Columns"
          input-save-button-text="Save"
          :input-data="BASE_FILTER_HEADERS_FOR_MANAGE_COLUMNS"
          :preselected-data="PRESELECTED_DATA"
          :reset-data="RESET_HEADERS"
          @saving-filter-data="updateFilter"
          @cancel-filter-data="showColumnsDialog = false"
        >
          <template #filter-title><span> Manage Columns </span></template>
        </BaseFilter>
      </v-dialog>

      <v-dialog v-model="dialog" width="900">
        <BaseEditRecord
          :form-field-headers="formFieldHeaders"
          :item-to-edit="editedItem.selectable"
          :ignore-to-edit="ignoreToEdit"
          :is-loading="loading"
          @handle-record-save="handleRecordSave"
          @cancel-filter-data="dialog = false"
        />
      </v-dialog>

      <v-dialog v-model="reportSummaryDialog" width="700">
        <BaseReportSummary
          :control-id="reportSummaryId"
          @close-view-report-summary="closeViewReportSummary"
        />
      </v-dialog>
    </div>
  </div>
</template>

<style scoped>
.width-select {
  width: 10em;
}
.submissions-search {
  width: 30em !important;
}
@media only screen and (max-width: 960px) {
  .submissions-search {
    max-width: 20em;
  }
}
@media (max-width: 599px) {
  .submissions-search {
    padding-left: 16px;
    padding-right: 16px;
  }
}

.submissions-table {
  clear: both;
}
@media (max-width: 1263px) {
  .submissions-table :deep(th) {
    vertical-align: top;
  }
}

.submissions-table :deep(thead tr th) {
  font-weight: normal;
  color: #003366 !important;
  font-size: 1.1em;
}

.style-1 {
  background-color: rgb(215, 215, 44);
}
.style-2 {
  background-color: rgb(114, 114, 67);
}
.width-max-content {
  width: max-content;
}

.justify-content-sp-bt {
  justify-content: space-between;
}

.page-title {
  width: 70% !important;
}
</style>
