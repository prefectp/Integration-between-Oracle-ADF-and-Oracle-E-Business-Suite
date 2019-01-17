package esr.nhs.bean;

import esr.nhs.am.NHS_AssignmentAppModuleImpl;

import esr.nhs.lovvo.OrganizationsLOVVORowImpl;
import esr.nhs.vo.AssignmentSearchVOImpl;

import esr.nhs.vo.AssignmentSearchVORowImpl;

import esr.nhs.vo.DateTrackHistoryVOImpl;

import esr.nhs.vo.GradeStepPlacementVOImpl;
import esr.nhs.vo.GradeStepPlacementVORowImpl;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.ParseException;

import java.util.Calendar;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.util.Set;

import javax.el.ELContext;

import javax.el.ExpressionFactory;

import javax.el.ValueExpression;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.faces.event.ValueChangeEvent;

import javax.servlet.http.HttpSession;

import nhs.esr.apiCall.CheckSegmentCombinationAPICall;
import nhs.esr.apiCall.CreateAndUpdateBudgetAPICall;
import nhs.esr.apiCall.CreateAndUpdateCostAPICall;
import nhs.esr.apiCall.CreateAssignmentAPICall;
import nhs.esr.apiCall.CreateGradeStepPlacementAPICall;
import nhs.esr.apiCall.SetPrimaryAssignmentAPICall;
import nhs.esr.apiCall.UpdateAssignmentAPICall;
import nhs.esr.apiCall.UpdateGradeStepPlacementAPICall;


import nhs.esr.apiCall.ValidateContractTypeAPICall;
import nhs.esr.util.ADFUtil;

import nhs.esr.util.DateUtil;
import nhs.esr.util.UIUtil;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputListOfValues;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;

import oracle.adf.view.rich.component.rich.layout.RichPanelHeader;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.adf.view.rich.event.PopupFetchEvent;
import oracle.adf.view.rich.event.ReturnPopupEvent;

import oracle.binding.BindingContainer;

import oracle.binding.OperationBinding;

import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.RowMatch;
import oracle.jbo.ViewObject;
import oracle.jbo.uicli.binding.JUCtrlListBinding;

import oracle.adf.view.rich.event.DialogEvent.Outcome;

import oracle.adf.view.rich.model.ListOfValuesModel;

import oracle.binding.AttributeBinding;

import oracle.jbo.RowSetIterator;
import oracle.jbo.domain.Number;
import oracle.jbo.server.ViewObjectImpl;
import oracle.jbo.uicli.binding.JUCtrlHierBinding;

import org.apache.myfaces.trinidad.model.RowKeySet;

public class AssignmentCreateBean {
    private RichSelectBooleanCheckbox primaryFlagBind;
    private RichInputListOfValues organizationNameInputLovBind;
    private RichInputListOfValues positionNameInputLovBind;
    private RichInputListOfValues gradeNameInputLovBind;
    private RichInputListOfValues locationCodeInputLovBind;
    private RichSelectOneChoice assgnCategoryLovBind;
    private RichSelectOneChoice changeReasonLovBind;
    private RichInputText groupKFFInptTxtBind;
    private RichInputListOfValues jobInputLovBind;
    private RichSelectOneChoice payrollLovBind;
    private RichSelectOneChoice assgnStatusLovBind;
    private RichSelectOneChoice empCategoryLovBind;
    private String groupDFFInptTxtVlaue;
    private RichPopup errorPopup;
    private String errorMessage;
    private RichPopup standardConditionForPositionPopup;
    private RichPopup defaultValuePositionPopup;
    private RichPopup locationForPositioinPopupBind;
    private String popUpBestActionMessage;
    private String assignmentCreationErrorSuccessMessage;
    private RichPopup nhsPeopleGroupKFFPopup;
    private RichInputText workingHoursBind;
    public Date currentDate = new Date();
    private RichSelectBooleanCheckbox workingAtHome;
    private RichSelectOneChoice frequencySelectOneBind;
    private RichSelectOneChoice hourlySalariedSelectOneBind;
    private RichSelectOneChoice noticePeriodUnits;
    private RichInputText noticePeriodLengthBind;
    private RichInputText probationPeriodLengthBind;
    private RichSelectOneChoice probationPeriodUnitsBind;
    private RichInputDate probationPeriodEndDateBind;
    private RichPopup assignmentCreationErrorSuccessPopup;
    private RichCommandButton createAssgnSaveButtonBind;
    private RichPopup messagePopup;
    private RichCommandButton updateButtonBind;
    private RichOutputText bestActionMessageBind;
    private RichSelectOneChoice payPointLOVBind;
    private BigDecimal peopleGroupId;
    public boolean futureChangesPresentMessage = false;
    public boolean showDetailItemIsDisable = false;
    public boolean primaryFlagIsDisable = false;
    public boolean standardConditionSaveButtonIsDisable = false;
    public boolean supervisorIsValid=true;
    public boolean changeReasonFlag = false;
    public boolean coreFormNavigationLinkIsDisable = false;
    private String assignmentFlowStatus = null;
    private String updationModeSet = null;
    private RichInputListOfValues locationNameBind;
    //private RichPopup changeReasonPopupBind;
    private RichPopup changeReasonPopUpBind;
    private RichSelectOneChoice assignmentStatusLOVBind;
    public String AssignmentStatus = null;
    // The below variables are added by Priya
    private BindingContainer bindings;
    private RichPopup locationConfirmationPopup;
    private RichInputListOfValues positionBind;
    private RichInputListOfValues gradeNameLovBind;
    private RichSelectOneChoice payrollLovforPositionBind;
    private RichInputListOfValues organizationNameBind;
    private RichInputListOfValues jobNameBind;
    private RichInputListOfValues supervisorNameBind;
    private RichInputListOfValues supervisorEmployeeNumberBind;
    private RichPopup updateAndCorreectionPopupBind;
    private RichPopup replaceAndInsertPopupBind;
    private RichPopup updateModeChangerPopupBind;

    private RichSelectOneChoice groupExpenseUserTypeLOV;
    private RichSelectOneChoice groupTimeAndAttendanceLOV;
    private RichSelectOneChoice groupDataEntryGroupLOV;
    // The above variables are added by Priya

    //------- variables added by A.Roy -------//
    private Number assignmentId;
    private BigDecimal placementId;
    private int rowIndex;
    private String gradeModeSet = null;
    private RichPopup updateAndCorrectionGradePopupBind;
    private RichPopup payrollChangePopUpBind;
    private Number baseGradeId;
    private Number basePositionId;
    private Number basePayrollId;
    private String baseGradeName;
    private String basePositionName;
    private String basePayrollName;
    private RichPopup validateContractTypePopup;
    private RichPopup replaceAndInsertPayrollPopupBind;
    private boolean payrollValueChangeListenerCalled = false;
    private boolean contractTypePopUpShown;
    private RichInputDate createUpdateEffDateBind;


    public AssignmentCreateBean() {
    }

    public String updateAssignment() {
        System.out.println(" in ...updateAssignment.........");
        assignmentFlowStatus = "UpdateAssignment";
        showDetailItemIsDisable = false;
        standardConditionSaveButtonIsDisable = false;
        coreFormNavigationLinkIsDisable =false;
        payrollValueChangeListenerCalled = false;
        contractTypePopUpShown = false;
        
        java.sql.Date effectiveDatesql = null;
        //====================================set effective date==========================================================================
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("p_effective_date") !=  null) {
            Date effectiveDate =
                (java.util.Date)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("p_effective_date");
            effectiveDatesql = new java.sql.Date(effectiveDate.getTime());
            System.out.println("...if ... effectiveDatesql...." +
                               effectiveDatesql);
        } else {
            effectiveDatesql =
                    new java.sql.Date(new java.util.Date().getTime());
            System.out.println("...else ... effectiveDatesql...." +
                               effectiveDatesql);
        }
        //============================================================================================================================
        String personId = null;
        String assignmentId = null;
        oracle.jbo.domain.Date assignmentEffEndDate = null;
        java.util.Date assignmentEffEndDate_util = null;

        DCBindingContainer bindings =(DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItrBindings = bindings.findIteratorBinding("AssignmentSearchVO1Iterator");
        ViewObject voTabledata = dcItrBindings.getViewObject();
        Row selectedRow = voTabledata.getCurrentRow();

        if (selectedRow.getAttribute("PersonId") != null)
            personId = ((Number)selectedRow.getAttribute("PersonId")).toString();
        if (selectedRow.getAttribute("AssignmentId") != null)
            assignmentId =((Number)selectedRow.getAttribute("AssignmentId")).toString();

        if (selectedRow.getAttribute("AssignmentEffEndDate") != null) {

            assignmentEffEndDate =(oracle.jbo.domain.Date)selectedRow.getAttribute("AssignmentEffEndDate");
            if ((assignmentEffEndDate.toString()).equalsIgnoreCase("4712-12-31")) {
                System.out.println(".....in if assignmentEffEndDate..4712-12-31.");
                futureChangesPresentMessage = false;
            } else {
                futureChangesPresentMessage = true;
            }

        } else {
            futureChangesPresentMessage = false;
        }
        
        baseGradeId = (Number)selectedRow.getAttribute("GradeId");
        basePayrollId = (Number)selectedRow.getAttribute("PayrollId");
        basePositionId = (Number)selectedRow.getAttribute("PositionId");
        baseGradeName = (String)selectedRow.getAttribute("GradeName");
        basePayrollName = (String)selectedRow.getAttribute("PayrollName");
        basePositionName = (String)selectedRow.getAttribute("PositionName");
        //convertDomainDatetoUtilDate(assignmentEffEndDate);
        // Date.convertDomainDatetoUtilDate();
        //========================createAssignmentDetailsBanner=====================================================================
        String full_name = selectedRow.getAttribute("FullName").toString();
        Number new_personId = (Number)selectedRow.getAttribute("PersonId");
        Number new_assignmentId =
            (Number)selectedRow.getAttribute("AssignmentId");
        String primaryFlag = (String)selectedRow.getAttribute("PrimaryFlag");
        createAssignmentDetailsBanner(full_name, new_personId,
                                      new_assignmentId, primaryFlag);
        //================================================================================================
        System.out.println("assignmentEffEndDate: " + assignmentEffEndDate);
        System.out.println("assignmentEffEndDate_util: " +
                           assignmentEffEndDate_util);

        System.out.println("PersonId: " + personId);
        System.out.println("assignmentId: " + assignmentId);
        System.out.println("full_name: " + full_name);

        //==============set assignment id in session=================================
        FacesContext fctx = FacesContext.getCurrentInstance();
          ExternalContext ectx = fctx.getExternalContext();
         HttpSession httpsession = (HttpSession)ectx.getSession(true);
         httpsession.setAttribute("assignmentId",assignmentId);
         Map<String, Object> scopeMap = ectx.getSessionMap();
         scopeMap.put("assignmentId",assignmentId);
        scopeMap.put("assignmentFlowStatus",assignmentFlowStatus);
        
        //==============for enable disable primary flag=================================
        if (primaryFlag.equalsIgnoreCase("Y")) {
            primaryFlagIsDisable = true;
        } else {
            primaryFlagIsDisable = false;
        }
        //primaryFlagIsDisable
        //===============================set date truck history==========================================================================

        NHS_AssignmentAppModuleImpl appmodule =
            (NHS_AssignmentAppModuleImpl)ADFUtil.getDataControl("NHS_AssignmentAppModuleDataControl");
        DateTrackHistoryVOImpl dateTrackHistoryVO =
            (DateTrackHistoryVOImpl)appmodule.getDateTrackHistoryVO1();
        dateTrackHistoryVO.setWhereClause(null);
        dateTrackHistoryVO.applyViewCriteria(null);

        System.out.println("....effectiveDatesql....++ " + effectiveDatesql);

        dateTrackHistoryVO.setWhereClause("assignment_id = '" + assignmentId + "'");

        dateTrackHistoryVO.executeQuery();
        System.out.println("...dateTrackHistoryVO....after .assignment_id.." +
                           dateTrackHistoryVO.getEstimatedRowCount());
        // ============================================================================================================================
        //Grade step record is verified here //
        validateAndInsertGradeStep();
        return "CreateUpdateAssignment";
    }


    public String createNewAssignmentNavigation() {
        assignmentFlowStatus = "CreateAssignment";
        showDetailItemIsDisable = true;
        primaryFlagIsDisable = true;
        standardConditionSaveButtonIsDisable = true;
        coreFormNavigationLinkIsDisable=true;
        
        DCBindingContainer bindings =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItrBindings =
            bindings.findIteratorBinding("AssignmentSearchVO1Iterator");
        ViewObject voTabledata = dcItrBindings.getViewObject();
        Row selectedRow = voTabledata.getCurrentRow();
        ViewObjectImpl voImpl = (ViewObjectImpl)dcItrBindings.getViewObject();
        Row row = voImpl.createRow();

        row.setAttribute("FullName", selectedRow.getAttribute("FullName"));
        row.setAttribute("PersonId", selectedRow.getAttribute("PersonId"));
        row.setAttribute("DateOfBirth",
                         selectedRow.getAttribute("DateOfBirth"));
        row.setAttribute("UserStatus", "Active Assignment");
        row.setAttribute("AssignmentStatusTypeId", 1);
        setAssignmentStatus("Active Assignment");
        System.out.println(".. row.setAttribut AssignmentStatusTypeId.." +
                           row.getAttribute("AssignmentStatusTypeId"));
        System.out.println(".. row.setAttribut UserStatus.." +
                           row.getAttribute("UserStatus"));

        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("p_effective_date") !=
            null) {
            row.setAttribute("AssignmentEffStartDate",
                             FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("p_effective_date"));
        } else {
            row.setAttribute("AssignmentEffStartDate",
                             new java.sql.Date(new java.util.Date().getTime()));
        }
        //========================createAssignmentDetailsBanner=====================================================================
        String full_name = selectedRow.getAttribute("FullName").toString();
        Number personId = (Number)selectedRow.getAttribute("PersonId");
        Number assignmentId = (Number)selectedRow.getAttribute("AssignmentId");
        String primaryFlag = (String)selectedRow.getAttribute("PrimaryFlag");
        createAssignmentDetailsBanner(full_name, personId, assignmentId,
                                      primaryFlag);
        //================================================================================================
        System.out.println("....FullName...." + row.getAttribute("FullName"));
        System.out.println("....PersonId...." + row.getAttribute("PersonId"));
        System.out.println("....FullName...." +
                           selectedRow.getAttribute("DateOfBirth"));

        voImpl.insertRow(row);
        //Grade step record is verified here //
        validateAndInsertGradeStep();

        return "CreateUpdateAssignment";
    }


    //==================used for create new assignment and update an existing assignment==========================================
public boolean validateOrganizationTrust(Row selectedRow,DCBindingContainer bindings) {
    if(selectedRow.getAttribute("OrganizationId") != null) {
        //oracle.jbo.domain.Number organizationId = (oracle.jbo.domain.Number) selectedRow.getAttribute("OrganizationId");
        DCIteratorBinding orgTrustTypeIter = bindings.findIteratorBinding("OrganizationTrustTypeVO1Iterator");
        ViewObject vo = orgTrustTypeIter.getViewObject();
        vo.setNamedWhereClauseParam("OrganizationId", selectedRow.getAttribute("OrganizationId"));
        vo.executeQuery();
        if(orgTrustTypeIter.getEstimatedRowCount() > 0) {
                return false;
        }
    }
    return true;
}

    public String assignmentRecordSave() {

        System.out.println("...in ...assignmentRecordSave ...method.");
        if(changeReasonFlag==true){
                UIUtil.showPopUp(changeReasonPopUpBind);
                return null;
            }
        //====================================set effective date==========================================================================
        java.sql.Date effectiveDatesql = null;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("p_effective_date") != null) {
            Date effectiveDate =  (java.util.Date)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("p_effective_date");
            effectiveDatesql = new java.sql.Date(effectiveDate.getTime());
            } else {
            effectiveDatesql =  new java.sql.Date(new java.util.Date().getTime());
            }
        //============================================================================================================================
        String personId = null;
       Number assignmentId = null;
        oracle.jbo.domain.Date assignmentEffEndDate = null;
        java.util.Date assignmentEffEndDate_util = null;

        DCBindingContainer bindings = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItrBindings = bindings.findIteratorBinding("AssignmentSearchVO1Iterator");
        ViewObject voTabledata = dcItrBindings.getViewObject();
        Row selectedRow = voTabledata.getCurrentRow();

        if (selectedRow.getAttribute("PersonId") != null)
            personId = ((Number)selectedRow.getAttribute("PersonId")).toString();
        if (selectedRow.getAttribute("AssignmentId") != null)
            assignmentId = (Number)selectedRow.getAttribute("AssignmentId");

        System.out.println("...personId ..." + personId);
        System.out.println("...assignmentId ..." + assignmentId);
        //validate organization trust
        if(!validateOrganizationTrust(selectedRow,bindings)){
            ADFUtil.showMessage("You can not create the assignment with the organization type of trust. Please selecte a new value from list of vales.", FacesMessage.SEVERITY_ERROR);
            return null;
        }
        if (assignmentId == null) {
            // creating a new assignment
            System.out.println(".....creating a new assignment..assignmentId.." +assignmentId);
            // ==============================================for testing purpose==========================================================
            System.out.println("...selectedRow.getAttribute...PersonId..." +selectedRow.getAttribute("PersonId"));
            System.out.println("...selectedRow.getAttribute...ORGANIZATION_NAME..." + selectedRow.getAttribute("OrganizationName"));
            System.out.println("...selectedRow.getAttribute...ORGANIZATION_id..." + selectedRow.getAttribute("OrganizationId"));
            System.out.println("...selectedRow.getAttribute...  POSITION_NAME..." +selectedRow.getAttribute("PositionName"));
            System.out.println("...selectedRow.getAttribute...poosition_id..." + selectedRow.getAttribute("PositionId"));
            System.out.println("...selectedRow.getAttribute...Grade_NAME..." +selectedRow.getAttribute("GradeName"));
            System.out.println("...selectedRow.getAttribute...Grade_id..." + selectedRow.getAttribute("GradeId"));
            System.out.println("...selectedRow.getAttribute...LocationCode..." +selectedRow.getAttribute("LocationCode"));
            System.out.println("...selectedRow.getAttribute...Location id..." + selectedRow.getAttribute("LocationId"));
            System.out.println("...selectedRow.getAttribute...ASSIGNMENT_CATEGORY..." + selectedRow.getAttribute("AssignmentCategory"));
            System.out.println("...selectedRow.getAttribute...ASG_CATEGORY_MEANING..." +selectedRow.getAttribute("AsgCategoryMeaning"));
            System.out.println("...selectedRow.getAttribute..EmploymentCategory..." +selectedRow.getAttribute("EmploymentCategory"));
            System.out.println("...selectedRow.getAttribute...JobName..." +selectedRow.getAttribute("JobName"));
            System.out.println("...selectedRow.getAttribute...Job-id..." +selectedRow.getAttribute("JobId"));
            System.out.println("...selectedRow.getAttribute...PayrollName..." + selectedRow.getAttribute("PayrollName"));
            System.out.println("...selectedRow.getAttribute..Payroll-id..." +selectedRow.getAttribute("PayrollId"));
            System.out.println("...selectedRow.getAttribute...USER_STATUS..." + selectedRow.getAttribute("UserStatus"));
            System.out.println("...selectedRow.getAttribute..ASSIGNMENT_STATUS_TYPE_ID..." +selectedRow.getAttribute("AssignmentStatusTypeId"));
            System.out.println("...selectedRow.getAttribute...EMP_CATEGORY_MEANING..." +selectedRow.getAttribute("EmpCategoryMeaning"));
            System.out.println("..selectedRow.getAttribute..EMPLOYEE_CATEGORY..." + selectedRow.getAttribute("EmployeeCategory"));
            System.out.println("...selectedRow.getAttribute..ChangeReason..." + selectedRow.getAttribute("ChangeReason"));

            // ==========================================================================================================================
            Object dateofbirth =(Object)selectedRow.getAttribute("DateOfBirth");
            Object payRollName =  (Object)selectedRow.getAttribute("PayrollName");
            
            // ==========================================================================================================================
            //=========================creating map=================================================================
            Map createAssignmentMap = new HashMap();
            //================for effective date setting=================================================================================
            java.util.Date effectiveDate = null;
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("p_effective_date") != null) {
                effectiveDate =  (java.util.Date)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("p_effective_date");
                java.sql.Date effectiveSqlDtaes =new java.sql.Date(effectiveDate.getTime());
                 createAssignmentMap.put("p_effective_date", effectiveSqlDtaes);
            } else {
                createAssignmentMap.put("p_effective_date", new java.sql.Date(new java.util.Date().getTime()));
            }
            //==========================================================================================================================
            // ====================================================setting in map========================================================
            //int p_org_id =Integer.parseInt((String)(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("orgId")));
            createAssignmentMap.put("p_business_group_id",62); //this is ok, as 62
            createAssignmentMap.put("p_person_id", selectedRow.getAttribute("PersonId"));
            createAssignmentMap.put("p_organization_id",selectedRow.getAttribute("OrganizationId"));
            createAssignmentMap.put("p_position_id",  selectedRow.getAttribute("PositionId"));
            createAssignmentMap.put("p_grade_id",selectedRow.getAttribute("GradeId"));
            createAssignmentMap.put("p_location_id", selectedRow.getAttribute("LocationId"));
            createAssignmentMap.put("p_job_id", selectedRow.getAttribute("JobId"));
            createAssignmentMap.put("p_change_reason", selectedRow.getAttribute("ChangeReason"));
            //EMPLOYMENT_CATEGORY is the assignment category
            createAssignmentMap.put("p_employment_category", selectedRow.getAttribute("EmploymentCategory"));
            createAssignmentMap.put("p_payroll_id",selectedRow.getAttribute("PayrollId"));
            createAssignmentMap.put("p_assignment_status_type_id", selectedRow.getAttribute("AssignmentStatusTypeId"));
            createAssignmentMap.put("p_employee_category",  selectedRow.getAttribute("EmployeeCategory"));
            // createAssignmentMap.put("p_people_group_id",getPeopleGroupId());
            //=============20-04-2016=============================================================
            createAssignmentMap.put("p_normal_hours", selectedRow.getAttribute("WorkingHours"));
            createAssignmentMap.put("p_frequency", selectedRow.getAttribute("Frequency"));
            createAssignmentMap.put("p_time_normal_start", selectedRow.getAttribute("TimeNormalStart"));
            createAssignmentMap.put("p_time_normal_finish",selectedRow.getAttribute("TimeNormalFinish"));
            createAssignmentMap.put("p_supervisor_id",selectedRow.getAttribute("SupervisorId"));
            createAssignmentMap.put("p_probation_period", selectedRow.getAttribute("ProbationPeriod"));
            createAssignmentMap.put("p_probation_unit", selectedRow.getAttribute("ProbationUnit"));
            createAssignmentMap.put("p_pay_basis_id",selectedRow.getAttribute("PayBasisId"));
            createAssignmentMap.put("p_date_probation_end",  selectedRow.getAttribute("DateProbationEnd"));
            
            System.out.println("...getPeopleGroupId..." + getPeopleGroupId());

            if (getPeopleGroupId() != null) {
                createAssignmentMap.put("p_people_group_id",getPeopleGroupId().intValue());
                System.out.println("...getPeopleGroupId..in if...." + getPeopleGroupId().intValue());

            } else {
                createAssignmentMap.put("p_people_group_id", 62); // should be default as 62, "Default Home|||"
                System.out.println("...getPeopleGroupId..in else..seting 83...." +getPeopleGroupId());
            }

            // ==========================================================================================================================

            ExternalContext ectx =FacesContext.getCurrentInstance().getExternalContext();
            Map<String, Object> scopeMap = ectx.getSessionMap();

            if ((dateofbirth == null) && payRollName != null) {
                System.out.println("..in payroll if...........");
                errorMessage ="You can not enter Payroll for this person. As selected person do not have Date of Birth. Please leave this field as blank.";
                UIUtil.showPopUp(errorPopup);
            }

            else {
                if (getAssignmentStatus() == null) {
                    errorMessage ="Assignment Status is a mandatory field. New assignment can not be created without assignment status. Please choose a assignment status.";
                    UIUtil.showPopUp(errorPopup);
                } else {
                    if (getAssignmentStatus().equalsIgnoreCase("Terminate Assignment") ||
                        getAssignmentStatus().equalsIgnoreCase("End") ||
                        getAssignmentStatus().equalsIgnoreCase("Terminate Application")) {
                        //An assignment status type with a system status of ACTIVE_ASSIGN must be specified.
                        errorMessage =  "Assignment Status type must be specified as Active Assignment.";
                        UIUtil.showPopUp(errorPopup);

                    } else {
                        try {
                            boolean isAssignmentCreated = false;
                            Map createAssignReturnMap =
                                (new CreateAssignmentAPICall()).callCreateAssignmentAPI(createAssignmentMap);
                            Set keys = createAssignReturnMap.keySet();
                            Iterator itr = keys.iterator();
                            String key = null;
                            Object value = null;
                            while (itr.hasNext()) {
                                key = (String)itr.next();
                                value = createAssignReturnMap.get(key);
                                if ("p_error".equalsIgnoreCase(key) &&
                                    (createAssignReturnMap.get("p_error") !=  null)) {
                                    System.out.println("+++bean++++Error: " +  createAssignReturnMap.get("p_error"));
                                    errorMessage = (String)createAssignReturnMap.get("p_error");
                                    UIUtil.showPopUp(errorPopup);
                                } else {
                                    System.out.println("+++Key++++" + key + "++++++++Value++++++++++" + createAssignReturnMap.get(key));
                                    String assignment_number = (String)createAssignReturnMap.get("p_assignment_number");
                                    BigDecimal assignment_id =(BigDecimal)createAssignReturnMap.get("p_assignment_id");
                                    System.out.println(" assignment_id" + assignment_id);
                                    if (assignment_number != null)
                                        scopeMap.put("assignment_number", assignment_number);

                                    if (assignment_id != null) {
                                        System.out.println("in if.. assignment_id..." + assignment_id);
                                        scopeMap.put("assignment_id", assignment_id);
                                        //===============================set date truck history==========================================================================
                                        NHS_AssignmentAppModuleImpl appmodule =(NHS_AssignmentAppModuleImpl)ADFUtil.getDataControl("NHS_AssignmentAppModuleDataControl");
                                        DateTrackHistoryVOImpl dateTrackHistoryVO =    (DateTrackHistoryVOImpl)appmodule.getDateTrackHistoryVO1();
                                        dateTrackHistoryVO.setWhereClause(null);
                                        dateTrackHistoryVO.applyViewCriteria(null);

                                        System.out.println("....effectiveDatesql....++ " +      effectiveDatesql);
                                        System.out.println("....assignment_id.toString()....++ " +  assignment_id.toString());

                                        dateTrackHistoryVO.setWhereClause(" assignment_id = '" + assignment_id.toString() + "'");
                                        dateTrackHistoryVO.executeQuery();
                                        System.out.println("...dateTrackHistoryVO....after .assignment_id.." + dateTrackHistoryVO.getEstimatedRowCount());
                                        // ============================================================================================================================
                                        //==============set assignment id in session=================================
                                         HttpSession httpsession = (HttpSession)ectx.getSession(true);
                                         httpsession.setAttribute("assignmentId",assignment_id.toString());
                                         scopeMap.put("assignmentId",assignment_id.toString());
                                        
                                       // assignment_id.toBigInteger();
                                        Object obj = assignment_id;
                                        selectedRow.setAttribute("AssignmentId",obj);
                                        selectedRow.setAttribute("AssignmentId",(Object)assignment_id);
                                        isAssignmentCreated = true;
                                        //======================================================================
                                        showDetailItemIsDisable = false;
                                        standardConditionSaveButtonIsDisable =false;
                                        coreFormNavigationLinkIsDisable=false;
                                        primaryFlagIsDisable=false;
                                        //===================================
                                       // errorMessage ="Assignment created successfully. New Assignment Number : "+assignment_number + ".";
                                        UIUtil.showPopUp(errorPopup);

                                    }

                                }
                            }
                            
                            boolean createCostFlag=false;
                               if(positionBind.getValue() !=null) {
                                     CreateAndUpdateCostAPICall createAndUpdateCostAPICall=  new CreateAndUpdateCostAPICall();
                                      createCostFlag=  createAndUpdateCostAPICall.createCostRecordForAssignmentDecision();
                               }
                            
                            if(createCostFlag){
                                    errorMessage ="Assignment created successfully. New Assignment Number : "+scopeMap.get("assignment_number") + ".\n"+
                                    "Costing Information has been updated for this Assignment.The Subjective Code has been set to " + scopeMap.get("subjectiveCode") +
                                                       ". Please navigate to the Costing form to review this.";
                                    UIUtil.showPopUp(errorPopup);
                                }
                            else{
                                    errorMessage ="Assignment created successfully. New Assignment Number : "+scopeMap.get("assignment_number") + ".";
                                    UIUtil.showPopUp(errorPopup);
                                }
                            //========calling budget api :: dont remove this=====
//                            if((workingHoursBind.getValue())!=null){
//                                 new CreateAndUpdateBudgetAPICall().createBudgetRecordForAssignment();
//                                 }
//                           
                            if(isAssignmentCreated) {
                                validateAndInsertGradeStep();
                            }
                            
                            //========calling refresh=====                            
                            return  refreshAssignmentAction(); 
                            
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }

        //updating an existing assignment... Code added by A.Roy

        else {
            //update assignment...updationModeSet
            System.out.println("....in updating an existing assignment....assignmentId..." + assignmentId);
            System.out.println("..update assignmnet flow.....updationModeSet...." + updationModeSet);
            AssignmentSearchVORowImpl selectedRowUpdate =  (AssignmentSearchVORowImpl)voTabledata.getCurrentRow();
            rowIndex = dcItrBindings.getCurrentRowIndexInRange();

//    if(supervisorIsValid==false){     
//                               errorMessage =  "Supervisor is not valid for the duration of the assignment. Please change supervisor name.";
//                               UIUtil.showPopUp(errorPopup);  
//                                } 
     // else{
         
          if (updationModeSet == null) {
                System.out.println("..update assignmnet flow.....updationModeSet..1.." + updationModeSet);
                UIUtil.showPopUp(updateAndCorreectionPopupBind);
            } else {
                Map updateAssignMap = new HashMap();
                java.util.Date effectiveDate = null;
                if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("p_effective_date") != null) {
                    effectiveDate = (java.util.Date)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("p_effective_date");
                    effectiveDatesql = new java.sql.Date(effectiveDate.getTime());
                     updateAssignMap.put("p_effective_date", effectiveDatesql);
                } else {
                    updateAssignMap.put("p_effective_date", new java.sql.Date(new java.util.Date().getTime()));
                }
                //Check whether grade is updated
                                Object gradeUpdatedObject = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("isGradeUpdated");
                                Boolean gradeUpdated = gradeUpdatedObject != null ? (Boolean)gradeUpdatedObject : false;
                                //if grade is updated validate grade step period
                                if(gradeUpdated)
                                {
                                if(updationModeSet.equals("CORRECTION")) {
                                    oracle.jbo.domain.Date assignmetnEffectiveDate = selectedRowUpdate.getAssignmentEffStartDate();
                                    if(assignmetnEffectiveDate != null) {
                                       if(!validateGradePeriod(assignmetnEffectiveDate.dateValue())) {
                                           ADFUtil.showMessage("Grade step placements are defined against this assignment in the future so you are unable to change the grade of the assignment before the start date of these records.",
                                                               FacesMessage.SEVERITY_ERROR);
                                           return null;
                                       }
                                    }
                                }
                                else{
                                    if(!validateGradePeriod(effectiveDatesql != null ? effectiveDatesql : new java.sql.Date(new java.util.Date().getTime()))) {
                                        ADFUtil.showMessage("Grade step placements are defined against this assignment in the future so you are unable to change the grade of the assignment before the start date of these records.",
                                                            FacesMessage.SEVERITY_ERROR);
                                        return null;
                                    }
                                    
                                    
                                }
                                }
                updateAssignMap.put("p_assignment_id", assignmentId);
              System.out.println("........p_datetrack_update_mode......."+updationModeSet);
                updateAssignMap.put("p_datetrack_update_mode",updationModeSet);
                updateAssignMap.put("p_grade_id",(Number)selectedRowUpdate.getGradeId());
                updateAssignMap.put("p_position_id", (Number)selectedRowUpdate.getPositionId());
                updateAssignMap.put("p_job_id", (Number)selectedRowUpdate.getJobId());
                updateAssignMap.put("p_payroll_id",(Number)selectedRowUpdate.getPayrollId());
               
                System.out.println("...p_payroll_id..update.."+selectedRowUpdate.getPayrollId());
                
                updateAssignMap.put("p_location_id",(Number)selectedRowUpdate.getLocationId());
                updateAssignMap.put("p_organization_id", (Number)selectedRowUpdate.getOrganizationId());
              
                System.out.println("...getOrganizationId..update.."+selectedRowUpdate.getOrganizationId());
                System.out.println("...p_location_id..update.."+selectedRowUpdate.getLocationId());

                updateAssignMap.put("p_pay_basis_id",(Integer)selectedRowUpdate.getPayBasisId());
                updateAssignMap.put("p_supervisor_id", (Number)selectedRowUpdate.getSupervisorId());
                updateAssignMap.put("p_assignment_number", (String)selectedRowUpdate.getAssignmentNumber());
                updateAssignMap.put("p_assignment_status_type_id", (Number)selectedRowUpdate.getAssignmentStatusTypeId());
                updateAssignMap.put("p_people_group_id", (BigDecimal)selectedRowUpdate.getPeopleGroupId());
                updateAssignMap.put("p_vacancy_id",(Number)selectedRowUpdate.getVacancyId());
                updateAssignMap.put("p_employee_category", (String)selectedRowUpdate.getEmployeeCategory());
                updateAssignMap.put("p_employment_category",(String)selectedRowUpdate.getEmploymentCategory());
                updateAssignMap.put("p_change_reason",  (String)selectedRowUpdate.getChangeReason());
                updateAssignMap.put("p_ass_attribute18",(String)selectedRowUpdate.getNhsJobApplRef());
                if (selectedRowUpdate.getFixedTermEndDate() != null){
                                    updateAssignMap.put("p_ass_attribute4", DateUtil.convertDateFormat((String)selectedRowUpdate.getFixedTermEndDate()));
                                }
                                else {
                                    updateAssignMap.put("p_ass_attribute4", null);
                                    }
                                
                updateAssignMap.put("p_ass_attribute5",(String)selectedRowUpdate.getFixedTermReason());
                updateAssignMap.put("p_ass_attribute6",(String)selectedRowUpdate.getLocalEmpContract());
                updateAssignMap.put("p_ass_attribute7",(String)selectedRowUpdate.getNewDeal());
                updateAssignMap.put("p_ass_attribute8", (String)selectedRowUpdate.getJobSharer());
                updateAssignMap.put("p_ass_attribute19", (String)selectedRowUpdate.getNightWorker());
                updateAssignMap.put("p_ass_attribute9",  (String)selectedRowUpdate.getFlexibleWorkingPattern());
                updateAssignMap.put("p_ass_attribute23", (String)selectedRowUpdate.getAvgWorkingDaysPerweek());
                updateAssignMap.put("p_ass_attribute25", (String)selectedRowUpdate.getMaximumParttime());
                    if (selectedRowUpdate.getFoundationGatewayDate() != null) {
                        updateAssignMap.put("p_ass_attribute17",
                                            DateUtil.convertDateFormat((String)selectedRowUpdate.getFoundationGatewayDate()));
                    } else {
                        updateAssignMap.put("p_ass_attribute17", null);
                    }
                    if (selectedRowUpdate.getAbsAccrualStartDate() != null) {
                        updateAssignMap.put("p_ass_attribute20",
                                            DateUtil.convertDateFormat((String)selectedRowUpdate.getAbsAccrualStartDate()));
                    } else {
                        updateAssignMap.put("p_ass_attribute20", null);
                    }
                    updateAssignMap.put("p_ass_attribute26",
                                        (String)selectedRowUpdate.getStartDateGrade());
                    if (selectedRowUpdate.getIncrementaDate() != null) {
                        updateAssignMap.put("p_ass_attribute1",
                                            DateUtil.convertDateFormat((String)selectedRowUpdate.getIncrementaDate()));
                    } else {
                        updateAssignMap.put("p_ass_attribute1", null);
                    }
                    if (selectedRowUpdate.getBankProposIncDate() != null) {
                        updateAssignMap.put("p_ass_attribute2",
                                            DateUtil.convertDateFormat((String)selectedRowUpdate.getBankProposIncDate()));
                    } else {
                        updateAssignMap.put("p_ass_attribute2", null);
                    }
                updateAssignMap.put("p_ass_attribute3", (String)selectedRowUpdate.getNoOfIncrements());
                updateAssignMap.put("p_ass_attribute29",(String)selectedRowUpdate.getDeptMgrOverride());
                updateAssignMap.put("p_ass_attribute30",(String)selectedRowUpdate.getMonitorChanges());
                updateAssignMap.put("p_ass_attribute10", (String)selectedRowUpdate.getOverrideStdGradeHours());
                updateAssignMap.put("p_ass_attribute11",(String)selectedRowUpdate.getOverrideStdGradeOtHours());
                updateAssignMap.put("p_ass_attribute12", (String)selectedRowUpdate.getOverrideStdGradeOtRate());
                if (selectedRowUpdate.getUpliftedRateChDt() != null) {
                        updateAssignMap.put("p_ass_attribute21",
                                            DateUtil.convertDateFormat((String)selectedRowUpdate.getUpliftedRateChDt()));
                    } else {
                        updateAssignMap.put("p_ass_attribute21", null);
                    }
                updateAssignMap.put("p_ass_attribute24", (String)selectedRowUpdate.getExitQuestionerReq());
                updateAssignMap.put("p_ass_attribute28", (String)selectedRowUpdate.getPayslipToHome());
                updateAssignMap.put("p_ass_attribute22",(String)selectedRowUpdate.getAccomidationStatus());
                updateAssignMap.put("p_ass_attribute13",(String)selectedRowUpdate.getFunding1());
                updateAssignMap.put("p_ass_attribute14",(String)selectedRowUpdate.getFunding1Percent());
                updateAssignMap.put("p_ass_attribute15", (String)selectedRowUpdate.getFunding2());
                updateAssignMap.put("p_ass_attribute16",(String)selectedRowUpdate.getFunding2Percent());
                //updateAssignMap.put("p_special_ceiling_step_id", (String)selectedRow.getProgres);
                updateAssignMap.put("p_supervisor_id",(Number)selectedRowUpdate.getSupervisorId());
                updateAssignMap.put("p_normal_hours",(Number)selectedRowUpdate.getWorkingHours());
                updateAssignMap.put("p_frequency", (String)selectedRowUpdate.getFrequency());
                updateAssignMap.put("p_hourly_salaried_code",(String)selectedRowUpdate.getHourlySalariedCode());
                updateAssignMap.put("p_time_normal_start", (String)selectedRowUpdate.getTimeNormalStart());
                updateAssignMap.put("p_time_normal_finish",(String)selectedRowUpdate.getTimeNormalFinish());
                updateAssignMap.put("p_work_at_home", (String)selectedRowUpdate.getWorkAtHome());
                updateAssignMap.put("p_notice_period",(Number)selectedRowUpdate.getNoticePeriodLength());
                updateAssignMap.put("p_notice_period_uom",  (String)selectedRowUpdate.getNoticePeriodUnits());
                updateAssignMap.put("p_special_ceiling_step_id", (BigDecimal)selectedRowUpdate.getSpecialCeilingStepId());
                //added by sibsankar
                updateAssignMap.put("p_employment_category",(String)selectedRowUpdate.getEmploymentCategory());
                updateAssignMap.put("p_probation_period", (BigDecimal)selectedRowUpdate.getProbationPeriod());
                updateAssignMap.put("p_probation_unit", (String)selectedRowUpdate.getProbationUnit());
                updateAssignMap.put("p_date_probation_end", selectedRowUpdate.getDateProbationEnd());
                updateAssignMap.put("p_supervisor_assignment_id", selectedRowUpdate.getSupervisorAssignmentId());
                updateAssignMap.put("p_manager_flag",(String)selectedRowUpdate.getManagerFlag());
                //==================================//end
                Map updateAssignmentReturnMap = new UpdateAssignmentAPICall().callUpdateAssignmentAPI(updateAssignMap);
                Set keys = updateAssignmentReturnMap.keySet();
                Iterator itr = keys.iterator();
                String key = null;
                Object value = null;
                while (itr.hasNext()) {
                    key = (String)itr.next();
                    value = updateAssignmentReturnMap.get(key);
        System.out.println("..value...while..."+value);
         System.out.println("..key...while..."+key);

                    if ("p_error".equalsIgnoreCase(key) && (updateAssignmentReturnMap.get("p_error") != null)) {
                        System.out.println("+++bean++++Error: " + updateAssignmentReturnMap.get("p_error"));
                        errorMessage =
                                (String)updateAssignmentReturnMap.get("p_error");
                        UIUtil.showPopUp(errorPopup);
                    } else {
                        
                        if( selectedRowUpdate.getPrimaryFlag() !=null) {
                            if( selectedRowUpdate.getPrimaryFlag().toString().equalsIgnoreCase("Y") && primaryFlagBind.isDisabled()==false && primaryFlagBind.isSelected()==true &&  ("p_effective_start_date".equalsIgnoreCase(key)))
                                 { 
                                   toMakePrimayAssignment( assignmentId, Integer.parseInt((String)personId));
                                     }
                                    else{
                                        Map sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
                                        sessionMap.put("isGradeUpdated", false);
                                        errorMessage = "Assignment details updated Successfully...!";
                                       UIUtil.showPopUp(errorPopup);
                                        }  
                                }
                                        else{
                                            Map sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
                                            sessionMap.put("isGradeUpdated", false);
                                            errorMessage = "Assignment details updated Successfully...!";
                                           UIUtil.showPopUp(errorPopup);
                                            }                  
                        }
                  }

            }
      //  }
    }
        
        if(updationModeSet !=null && updationModeSet.trim().length()>0) {
            //  if(updationModeSet.equalsIgnoreCase("UPDATE")){
                  return  refreshAssignmentAction();            
            //    }
            }
            
        updationModeSet = null;
        return null;
    }


    private void createAssignmentDetailsBanner(String full_name,
                                               Number personId,
                                               Number assignmentId,
                                               String primaryFlag) {

        String assignmentHeaderBanner = "WTE: ";
        System.out.println("...in createAssignmentDetailsBanner.. ");
        ExternalContext ectx =
            FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> scopeMap = ectx.getSessionMap();
        System.out.println("..get from session.noOfAssignment.." +
                           scopeMap.get("total_Number_Of_Assignment"));
        System.out.println("...full_name.." + full_name);
        System.out.println("...personId.." + personId);
        System.out.println("...assignmentId.." + assignmentId);
        System.out.println("...primaryFlag.." + primaryFlag);
        // =======================================setting effective date==============================================================
        java.util.Date effectiveDate = null;
        java.sql.Date effectiveDatesql = null;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("p_effective_date") !=
            null) {
            effectiveDate =
                    (java.util.Date)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("p_effective_date");
            System.out.println("++++ effectiveDate in if++ " + effectiveDate);
            effectiveDatesql = new java.sql.Date(effectiveDate.getTime());
            System.out.println("++++ effectiveDate in if++ " +
                               effectiveDatesql);
        } else {
            System.out.println("++++ effectiveDate in else ++ " +
                               effectiveDate);
            effectiveDatesql =
                    new java.sql.Date(new java.util.Date().getTime());
            System.out.println("++++ effectiveDate in else ++ " +
                               effectiveDatesql);
        }
        // =====================================================================================================

        // =======================================for WTE==============================================================
        NHS_AssignmentAppModuleImpl appmodule =
            (NHS_AssignmentAppModuleImpl)ADFUtil.getDataControl("NHS_AssignmentAppModuleDataControl");
        ViewObject assignmentBudgetValuesfVO =
            appmodule.getAssignmentBudgetValuesF1();
        assignmentBudgetValuesfVO.setWhereClause("to_date('" +
                                                 effectiveDatesql +
                                                 "', 'YYYY-MM-DD') BETWEEN trunc(effective_start_date) and trunc(effective_end_Date)\n" +
                "and assignment_id = '" + assignmentId + "'");
        assignmentBudgetValuesfVO.executeQuery();
        if (assignmentBudgetValuesfVO.getEstimatedRowCount() > 0 &&
            assignmentFlowStatus.equalsIgnoreCase("UpdateAssignment")) {
            BigDecimal valueWTE =
                ((BigDecimal)assignmentBudgetValuesfVO.first().getAttribute("Value"));
            System.out.println("....valueWTE...." + valueWTE);
            assignmentHeaderBanner = assignmentHeaderBanner + valueWTE + ". ";
            //System.out.println("...1...assignmentHeaderBanner..."+assignmentHeaderBanner);
            //if (valueWTE !=null) {  }
        } else {
            System.out.println("valueWTE not set..else");
            assignmentHeaderBanner = assignmentHeaderBanner + "Not set. ";
            // System.out.println("...2...assignmentHeaderBanner..."+assignmentHeaderBanner);
        }
        // =======================================for primay assignment set============================================
        if (primaryFlag.equalsIgnoreCase("Y") &&
            assignmentFlowStatus.equalsIgnoreCase("UpdateAssignment")) {
            assignmentHeaderBanner =
                    assignmentHeaderBanner + "Primary assignment. ";
            //  System.out.println("...3...assignmentHeaderBanner..."+assignmentHeaderBanner);
        } else {
            assignmentHeaderBanner =
                    assignmentHeaderBanner + "Non-Primary assignment. ";
            //  System.out.println("...4...assignmentHeaderBanner..."+assignmentHeaderBanner);
        }
        scopeMap.get("total_Number_Of_Assignment");
        assignmentHeaderBanner =
                assignmentHeaderBanner + scopeMap.get("total_Number_Of_Assignment") +
                " assignments in total. ";
        // System.out.println("...5...assignmentHeaderBanner..."+assignmentHeaderBanner);
        // =======================================for sum WTE==============================================================
        BigDecimal sumValueWTE = null;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ADFUtil.getConnection();
            String sqlQuery =
                "select (sum(value)) as SumValueWTE from PER_ASSIGNMENT_BUDGET_VALUES_F where assignment_id IN (select assignment_id from per_all_assignments_f where person_id=? and ? between effective_start_date and effective_end_Date)";
            statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, personId.intValue());
            statement.setDate(2, effectiveDatesql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                sumValueWTE = (BigDecimal)rs.getObject("SumValueWTE");
                System.out.println("...sumValueWTE..." + sumValueWTE);
                break; //as we are expecting only one row
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        if (sumValueWTE != null) {
            assignmentHeaderBanner =
                    assignmentHeaderBanner + " Total WTE: " + sumValueWTE +
                    ". ";
            //System.out.println("...6...assignmentHeaderBanner..."+assignmentHeaderBanner);
        } else {
            assignmentHeaderBanner =
                    assignmentHeaderBanner + " Total WTE: " + "Not set. ";
            // System.out.println("...7...assignmentHeaderBanner..."+assignmentHeaderBanner);
        }

        assignmentHeaderBanner =
                assignmentHeaderBanner + " (" + full_name + ")";
        System.out.println("...8...assignmentHeaderBanner..." +
                           assignmentHeaderBanner);
        scopeMap.put("assignmentHeaderBanner", assignmentHeaderBanner);
    }


    public void dialogListenerKFFGroup(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome() == DialogEvent.Outcome.ok) {
            String nhsPeopleGroupFlexfield = null;
            BigDecimal selected_paypoint_id = null;
            String selected_payPoint =
                (String)UIUtil.getSelectedValueFromLOV("PaypointLOVVO1");

            //===============to get pay point id====================================
            // String selected_payPointName =(String)payPointLOVBind.getValue();
            NHS_AssignmentAppModuleImpl appmodule =
                (NHS_AssignmentAppModuleImpl)ADFUtil.getDataControl("NHS_AssignmentAppModuleDataControl");
            System.out.println(".....selected_payPointName..." +
                               selected_payPoint);
            if (selected_payPoint != null &&
                selected_payPoint.trim().length() > 0) {
                ViewObject payPointVO = appmodule.getPaypointLOVVO1();
                payPointVO.setWhereClause(" LOCATION_CODE = '" +
                                          selected_payPoint + "'");
                payPointVO.executeQuery();
                selected_paypoint_id =
                        ((BigDecimal)payPointVO.first().getAttribute("LocationId"));
                System.out.println("++++  selected_location_id +++++" +
                                   selected_paypoint_id);
                //                createAssignmentMap.put("p_location_id", selected_location_id) ;
            }
            //=======================================================================
            String selected_expenseUserType =
                (String)UIUtil.getSelectedValueFromLOV("ExpenseUserTypeLOVVO1");
            String selected_timeAndAttendance =
                (String)UIUtil.getSelectedValueFromLOV("TimeAndAttendanceLOVVO1");
            String selected_dataEntryGroup =
                (String)UIUtil.getSelectedValueFromLOV("DataEntryGroupLOVVO1");

            //===============================logic to retrieve KFF value=================================================================
            //=======================get people_group_id from PAY_PEOPLE_GROUPS_KFV=======================================================

            String groupKFF_topass_into_api =
                selected_paypoint_id.toString() + "|";
            if (selected_expenseUserType != null &&
                selected_expenseUserType.trim().length() > 0)
                groupKFF_topass_into_api =
                        groupKFF_topass_into_api + selected_expenseUserType +
                        "|";
            else
                groupKFF_topass_into_api = groupKFF_topass_into_api + "|";


            if (selected_timeAndAttendance != null &&
                selected_timeAndAttendance.trim().length() > 0)
                groupKFF_topass_into_api =
                        groupKFF_topass_into_api + selected_timeAndAttendance +
                        "|";
            else
                groupKFF_topass_into_api = groupKFF_topass_into_api + "|";

            if (selected_dataEntryGroup != null &&
                selected_dataEntryGroup.trim().length() > 0)
                groupKFF_topass_into_api =
                        groupKFF_topass_into_api + selected_dataEntryGroup +
                        "|";

            System.out.println("...hi...........groupKFF_topass_into_api.....new ..." +
                               groupKFF_topass_into_api);
            //            else
            //                groupKFF_topass_into_api = groupKFF_topass_into_api+ "|" ;

            //+ selected_expenseUserType + "|" + selected_timeAndAttendance + "|" +selected_dataEntryGroup ;
            System.out.println(".............groupKFF_topass_into_api.........." +
                               groupKFF_topass_into_api);

            ViewObject payPeopleGroupKFFVO =
                appmodule.getPayPeopleGroupKFFVO1();
            payPeopleGroupKFFVO.setWhereClause(" concatenated_segments = '" +
                                               groupKFF_topass_into_api + "'");
            payPeopleGroupKFFVO.executeQuery();
            if (payPeopleGroupKFFVO.getEstimatedRowCount() > 0) {
                peopleGroupId =
                        ((BigDecimal)payPeopleGroupKFFVO.first().getAttribute("PeopleGroupId"));
            }
            // System.out.println(".............peopleGroupId......."+peopleGroupId);

            DCBindingContainer bindings =
                (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
            DCIteratorBinding dcItrBindings =
                bindings.findIteratorBinding("AssignmentSearchVO1Iterator");
            ViewObject voTabledata = dcItrBindings.getViewObject();
            Row selectedRow = voTabledata.getCurrentRow();
            if (peopleGroupId != null &&
                payPeopleGroupKFFVO.getEstimatedRowCount() > 0) {
                System.out.println(".............peopleGroupId..if....." +
                                   peopleGroupId);
                selectedRow.setAttribute("PeopleGroupId", peopleGroupId);
                //PEOPLE_GROUP_ID
                setPeopleGroupId(peopleGroupId);
            }
            //===============================================================================================================

            else {
                    Map checkSegmentCombinationMap = new HashMap();
                    checkSegmentCombinationMap.put("p_paypoint_location", selected_paypoint_id);
                    checkSegmentCombinationMap.put("p_expense_user_type", selected_expenseUserType);
                    checkSegmentCombinationMap.put("p_time_and_attendance", selected_timeAndAttendance);
                    checkSegmentCombinationMap.put("p_data_entry_group", selected_dataEntryGroup);
                    Map checkSegmentCombinationReturnMap = new CheckSegmentCombinationAPICall().callCheckSegmenCombinationtAPI(checkSegmentCombinationMap);
                    Set keys = checkSegmentCombinationReturnMap.keySet();
                    Iterator itr = keys.iterator();
                    String key = null;
                    Object value = null;
                    // boolean flagset=true;
                    while (itr.hasNext()) {
                        key = (String)itr.next();
                        value = checkSegmentCombinationReturnMap.get(key);
                                                
                            if ("p_error".equalsIgnoreCase(key) && (checkSegmentCombinationReturnMap.get("p_error") != null)) {
                                System.out.println("+++bean++++Error: " + checkSegmentCombinationReturnMap.get("p_error"));
                                errorMessage =  (String)checkSegmentCombinationReturnMap.get("p_error");
                              //  flagset=false;
                                UIUtil.showPopUp(errorPopup);
                            }
                            else{
                            System.out.println("+++Key+++: "+key+"+++Value++++"+checkSegmentCombinationReturnMap.get(key));
                                peopleGroupId = (BigDecimal)checkSegmentCombinationReturnMap.get("p_ccid");
                        }
                    }
                    selectedRow.setAttribute("PeopleGroupId", peopleGroupId);
                    setPeopleGroupId(peopleGroupId);
                    /* need to call api to insert a new row of new combination into PAY_PEOPLE_GROUPS_KFV table*/
                    //need to set the new PeopleGroupId into  peopleGroupId variable
                }
            
            //=============================================================================

       
            if (selected_payPoint != null &&
                selected_payPoint.trim().length() > 0) {
                System.out.println("nhsPeopleGroupFlexfield ++ " +
                                   nhsPeopleGroupFlexfield);
                nhsPeopleGroupFlexfield = selected_payPoint + "|";
                System.out.println("nhsPeopleGroupFlexfield ++ " +
                                   nhsPeopleGroupFlexfield);
                nhsPeopleGroupFlexfield =
                        nhsPeopleGroupFlexfield + selected_expenseUserType +
                        "|";
                System.out.println("nhsPeopleGroupFlexfield ++ " +
                                   nhsPeopleGroupFlexfield);
                nhsPeopleGroupFlexfield =
                        nhsPeopleGroupFlexfield + selected_timeAndAttendance +
                        "|";
                System.out.println("nhsPeopleGroupFlexfield ++ " +
                                   nhsPeopleGroupFlexfield);
                nhsPeopleGroupFlexfield =
                        nhsPeopleGroupFlexfield + selected_dataEntryGroup;
                System.out.println("nhsPeopleGroupFlexfield ++ " +
                                   nhsPeopleGroupFlexfield);
                groupKFFInptTxtBind.setValue(nhsPeopleGroupFlexfield);
                AdfFacesContext.getCurrentInstance().addPartialTarget(groupKFFInptTxtBind);
            } else {
                errorMessage =
                        "Paypoint is mandatory field. Please select a Paypoint value to continue.";
                UIUtil.showPopUp(errorPopup);
                UIUtil.showPopUp(nhsPeopleGroupKFFPopup);
                // open error popup for paypoint
            }
        

        } else {

            System.out.println("in cancel popup");
        }
    }

    public void setPrimaryFlagBind(RichSelectBooleanCheckbox primaryFlagBind) {
        this.primaryFlagBind = primaryFlagBind;
    }

    public RichSelectBooleanCheckbox getPrimaryFlagBind() {
        return primaryFlagBind;
    }

    public void setOrganizationNameInputLovBind(RichInputListOfValues organizationNameInputLovBind) {
        this.organizationNameInputLovBind = organizationNameInputLovBind;
    }

    public RichInputListOfValues getOrganizationNameInputLovBind() {
        return organizationNameInputLovBind;
    }


    public void setPositionNameInputLovBind(RichInputListOfValues positionNameInputLovBind) {
        this.positionNameInputLovBind = positionNameInputLovBind;
    }

    public RichInputListOfValues getPositionNameInputLovBind() {
        return positionNameInputLovBind;
    }

    public void setGradeNameInputLovBind(RichInputListOfValues gradeNameInputLovBind) {
        this.gradeNameInputLovBind = gradeNameInputLovBind;
    }

    public RichInputListOfValues getGradeNameInputLovBind() {
        return gradeNameInputLovBind;
    }

    public void setLocationCodeInputLovBind(RichInputListOfValues locationCodeInputLovBind) {
        this.locationCodeInputLovBind = locationCodeInputLovBind;
    }

    public RichInputListOfValues getLocationCodeInputLovBind() {
        return locationCodeInputLovBind;
    }

    public void setAssgnCategoryLovBind(RichSelectOneChoice assgnCategoryLovBind) {
        this.assgnCategoryLovBind = assgnCategoryLovBind;
    }

    public RichSelectOneChoice getAssgnCategoryLovBind() {
        return assgnCategoryLovBind;
    }

    public void setChangeReasonLovBind(RichSelectOneChoice changeReasonLovBind) {
        this.changeReasonLovBind = changeReasonLovBind;
    }

    public RichSelectOneChoice getChangeReasonLovBind() {
        return changeReasonLovBind;
    }


    public void setJobInputLovBind(RichInputListOfValues jobInputLovBind) {
        this.jobInputLovBind = jobInputLovBind;
    }

    public RichInputListOfValues getJobInputLovBind() {
        return jobInputLovBind;
    }

    public void setPayrollLovBind(RichSelectOneChoice payrollLovBind) {
        this.payrollLovBind = payrollLovBind;
    }

    public RichSelectOneChoice getPayrollLovBind() {
        return payrollLovBind;
    }

    public void setAssgnStatusLovBind(RichSelectOneChoice assgnStatusLovBind) {
        this.assgnStatusLovBind = assgnStatusLovBind;
    }

    public RichSelectOneChoice getAssgnStatusLovBind() {
        return assgnStatusLovBind;
    }

    public void setEmpCategoryLovBind(RichSelectOneChoice empCategoryLovBind) {
        this.empCategoryLovBind = empCategoryLovBind;
    }

    public RichSelectOneChoice getEmpCategoryLovBind() {
        return empCategoryLovBind;
    }

    public void setGroupDFFInptTxtVlaue(String groupDFFInptTxtVlaue) {
        this.groupDFFInptTxtVlaue = groupDFFInptTxtVlaue;
    }

    public String getGroupDFFInptTxtVlaue() {
        return groupDFFInptTxtVlaue;
    }


    public void setErrorPopup(RichPopup errorPopup) {
        this.errorPopup = errorPopup;
    }

    public RichPopup getErrorPopup() {
        return errorPopup;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }


    public void setWorkingHoursBind(RichInputText workingHoursBind) {
        this.workingHoursBind = workingHoursBind;
    }

    public RichInputText getWorkingHoursBind() {
        return workingHoursBind;
    }


    public void setWorkingAtHome(RichSelectBooleanCheckbox workingAtHome) {
        this.workingAtHome = workingAtHome;
    }

    public RichSelectBooleanCheckbox getWorkingAtHome() {
        return workingAtHome;
    }

    public void workingHoursValueChangeListener(ValueChangeEvent valueChangeEvent) {
        System.out.println(" in...");
        if (workingHoursBind.getValue() != null &&
            valueChangeEvent.getNewValue() != null &&
            valueChangeEvent.getNewValue().toString().trim().length() > 0) {
            System.out.println(" in...if");
            frequencySelectOneBind.setDisabled(false);
            frequencySelectOneBind.setRequired(true);
            hourlySalariedSelectOneBind.setDisabled(false);
            hourlySalariedSelectOneBind.setRequired(true);

        } else {
            System.out.println(" in..else.");
            NHS_AssignmentAppModuleImpl am =
                (NHS_AssignmentAppModuleImpl)ADFUtil.getDataControl("NHS_AssignmentAppModuleDataControl");
            ViewObject frequencyVO = am.getFrequencyLOVVO1();
            ViewObject hourlySalariedVO = am.getHourlySalariedLOVVO1();
            frequencyVO.executeQuery();
            hourlySalariedVO.executeQuery();
            frequencySelectOneBind.setDisabled(true);
            hourlySalariedSelectOneBind.setDisabled(true);
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(frequencySelectOneBind);
        AdfFacesContext.getCurrentInstance().addPartialTarget(hourlySalariedSelectOneBind);
    }

    public void noticePeriodLengthValueChangeListener(ValueChangeEvent valueChangeEvent) {
        System.out.println(" in...");
        if (noticePeriodLengthBind.getValue() != null &&
            valueChangeEvent.getNewValue() != null &&
            valueChangeEvent.getNewValue().toString().trim().length() > 0) {
            System.out.println(" in...if");
            noticePeriodUnits.setDisabled(false);
            noticePeriodUnits.setRequired(true);

        } else {
            System.out.println(" in..else.");
            noticePeriodUnits.setDisabled(true);
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(noticePeriodUnits);
    }

    public void probationPeriodLengthValueChangeListener(ValueChangeEvent valueChangeEvent) {
        System.out.println(" in...");
        if (probationPeriodLengthBind.getValue() != null &&
            valueChangeEvent.getNewValue() != null &&
            valueChangeEvent.getNewValue().toString().trim().length() > 0) {
            System.out.println(" in...if");
            probationPeriodUnitsBind.setDisabled(false);
            probationPeriodUnitsBind.setRequired(true);
        } else {
            System.out.println(" in..else.");
            NHS_AssignmentAppModuleImpl am =
                (NHS_AssignmentAppModuleImpl)ADFUtil.getDataControl("NHS_AssignmentAppModuleDataControl");
            ViewObject probationPeriodUnitsVO =
                am.getProbationPeriodUnitsLOVVO1();
            // assignmentSearchVO.resetExecuted();
            probationPeriodUnitsVO.executeQuery();
            probationPeriodUnitsBind.setDisabled(true);
            probationPeriodEndDateBind.setValue(null);
            AdfFacesContext.getCurrentInstance().addPartialTarget(probationPeriodEndDateBind);
        }
        // AdfFacesContext.getCurrentInstance().addPartialTarget(probationPeriodUnitsBind);
    }

    
    public void probationPeriodUnitsValueChangeListener(ValueChangeEvent valueChangeEvent) {

        Integer propbationUnitIndex =
            valueChangeEvent.getNewValue() != null ? (Integer)valueChangeEvent.getNewValue() :
            null;
        System.out.println(" propbationUnit +++++ " + propbationUnitIndex);
        DCBindingContainer dcBindings =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding iterBind =
            (DCIteratorBinding)dcBindings.get("AssignmentSearchVO1Iterator");
        Row currentAssignment = iterBind.getCurrentRow();
        Object probationPeriodLength =
            currentAssignment.getAttribute("ProbationPeriod");
        System.out.println("probationPeriodLengthBind...." +
                           probationPeriodLength);
        Integer probationPeriodLengthInt =
            new Integer(probationPeriodLength.toString());
        this.setValueToEL("#{bindings.ProbationUnit.inputValue}",
                          valueChangeEvent.getNewValue()); //Updates the model
        System.out.println("\n******** Selected Value: " +
                           resolveExpression("#{bindings.ProbationUnit.attributeValue}"));
        Object probationUnitObj =
            resolveExpression("#{bindings.ProbationUnit.attributeValue}");
        String probationUnit =
            probationUnitObj != null ? (String)probationUnitObj : null;

        oracle.jbo.domain.Number personId =
            currentAssignment.getAttribute("PersonId") != null ?
            (Number)currentAssignment.getAttribute("PersonId") : null;

        //Date probationStartDate = getProbationStartDate(personId);
        
        Date probationStartDate = currentAssignment.getAttribute("AssignmentId") !=null ? getMinOfAssignmentDates(dcBindings) : null;
        
        if(probationStartDate == null) {
           oracle.jbo.domain.Date assignmentStartDate = currentAssignment.getAttribute("AssignmentEffStartDate") != null ? (oracle.jbo.domain.Date)currentAssignment.getAttribute("AssignmentEffStartDate") : null;
           probationStartDate = assignmentStartDate != null ? assignmentStartDate.dateValue() : null;
        }
        if (probationStartDate != null) {

            Calendar cal = Calendar.getInstance();
            cal.setTime(probationStartDate);
            if ("D".equals(probationUnit)) {
                cal.add(Calendar.DATE, probationPeriodLengthInt);
            } else if ("H".equals(probationUnit)) {
                cal.add(Calendar.HOUR, probationPeriodLengthInt);
            } else if ("M".equals(probationUnit)) {
                cal.add(Calendar.MONTH, probationPeriodLengthInt);
            } else if ("W".equals(probationUnit)) {
                cal.add(Calendar.WEEK_OF_MONTH, probationPeriodLengthInt);
            } else if ("Y".equals(probationUnit)) {
                cal.add(Calendar.YEAR, probationPeriodLengthInt);
            }
            java.sql.Date sqldate = new java.sql.Date(cal.getTimeInMillis());
            oracle.jbo.domain.Date domainDate =
                new oracle.jbo.domain.Date(sqldate);
            //currentAssignment.setAttribute("DateProbationEnd", sqldate);
            currentAssignment.setAttribute("DateProbationEnd", domainDate);
            //this.setValueToEL("#{bindings.DateProbationEnd.inputValue}", domainDate);
            //probationPeriodEndDateBind.setValue(sqldate);
            //setProbationPeriodEndDateBind(probationPeriodEndDateBind);

        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(probationPeriodEndDateBind);
    }
    private Date getMinOfAssignmentDates(DCBindingContainer dcBindings) {
        DCIteratorBinding iterBind =
            (DCIteratorBinding)dcBindings.get("DateTrackHistoryVO1Iterator");
        iterBind.executeQuery();
        if(iterBind.getEstimatedRowCount() >0 ) {
            ViewObject vo = iterBind.getViewObject();
            Object startDate = vo.first().getAttribute("AssignmentEffStartDate");
            return startDate!= null ? (Date)startDate : null;
        }
        return null;
    }
    private Date getProbationStartDate(Number personId) {
        if (personId != null) {
            Key personKey = new Key(new Object[] { personId });
            NHS_AssignmentAppModuleImpl assignmentAppModule =
                (NHS_AssignmentAppModuleImpl)ADFUtil.getDataControl("NHS_AssignmentAppModuleDataControl");
            Row[] persons =
                assignmentAppModule.getPeriodsOfServiceVO1().findByKey(personKey,
                                                                       1);

            if (persons != null && persons.length > 0) {
                Row person = persons[0];
                if (person.getAttribute("DateStart") != null) {
                    java.sql.Date sqlDate =
                        (java.sql.Date)person.getAttribute("DateStart");
                    Date probationStartDate = new Date(sqlDate.getTime());
                    return probationStartDate;
                }

            }
        }
        return null;
    }

    public Object resolveExpression(String el) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory =
            facesContext.getApplication().getExpressionFactory();
        ValueExpression valueExp =
            expressionFactory.createValueExpression(elContext, el,
                                                    Object.class);
        return valueExp.getValue(elContext);
    }

    public void setValueToEL(String el, Object val) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory =
            facesContext.getApplication().getExpressionFactory();
        ValueExpression exp =
            expressionFactory.createValueExpression(elContext, el,
                                                    Object.class);
        exp.setValue(elContext, val);
    }

    public void setFrequencySelectOneBind(RichSelectOneChoice frequencySelectOneBind) {
        this.frequencySelectOneBind = frequencySelectOneBind;
    }

    public RichSelectOneChoice getFrequencySelectOneBind() {
        return frequencySelectOneBind;
    }

    public void setHourlySalariedSelectOneBind(RichSelectOneChoice hourlySalariedSelectOneBind) {
        this.hourlySalariedSelectOneBind = hourlySalariedSelectOneBind;
    }

    public RichSelectOneChoice getHourlySalariedSelectOneBind() {
        return hourlySalariedSelectOneBind;
    }

    public void setNoticePeriodUnits(RichSelectOneChoice noticePeriodUnits) {
        this.noticePeriodUnits = noticePeriodUnits;
    }

    public RichSelectOneChoice getNoticePeriodUnits() {
        return noticePeriodUnits;
    }


    public void setNoticePeriodLengthBind(RichInputText noticePeriodLengthBind) {
        this.noticePeriodLengthBind = noticePeriodLengthBind;
    }

    public RichInputText getNoticePeriodLengthBind() {
        return noticePeriodLengthBind;
    }


    public void setProbationPeriodLengthBind(RichInputText probationPeriodLengthBind) {
        this.probationPeriodLengthBind = probationPeriodLengthBind;
    }

    public RichInputText getProbationPeriodLengthBind() {
        return probationPeriodLengthBind;
    }

    public void setProbationPeriodUnitsBind(RichSelectOneChoice probationPeriodUnitsBind) {
        this.probationPeriodUnitsBind = probationPeriodUnitsBind;
    }

    public RichSelectOneChoice getProbationPeriodUnitsBind() {
        return probationPeriodUnitsBind;
    }

    public void setProbationPeriodEndDateBind(RichInputDate probationPeriodEndDateBind) {
        this.probationPeriodEndDateBind = probationPeriodEndDateBind;
    }

    public RichInputDate getProbationPeriodEndDateBind() {
        return probationPeriodEndDateBind;
    }

    public String backToAssignmentSearchPage() {
        Map sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.put("isGradeUpdated", false);
        UIUtil.sendRedirect(null);
        return null;
    }


    public void setAssignmentCreationErrorSuccessPopup(RichPopup assignmentCreationErrorSuccessPopup) {
        this.assignmentCreationErrorSuccessPopup =
                assignmentCreationErrorSuccessPopup;
    }

    public RichPopup getAssignmentCreationErrorSuccessPopup() {
        return assignmentCreationErrorSuccessPopup;
    }

    public void setAssignmentCreationErrorSuccessMessage(String assignmentCreationErrorSuccessMessage) {
        this.assignmentCreationErrorSuccessMessage =
                assignmentCreationErrorSuccessMessage;
    }

    public String getAssignmentCreationErrorSuccessMessage() {
        return assignmentCreationErrorSuccessMessage;
    }

   
    public void setCreateAssgnSaveButtonBind(RichCommandButton createAssgnSaveButtonBind) {
        this.createAssgnSaveButtonBind = createAssgnSaveButtonBind;
    }

    public RichCommandButton getCreateAssgnSaveButtonBind() {
        return createAssgnSaveButtonBind;
    }

    public void setMessagePopup(RichPopup messagePopup) {
        this.messagePopup = messagePopup;
    }

    public RichPopup getMessagePopup() {
        return messagePopup;
    }

    public void setPopUpBestActionMessage(String popUpBestActionMessage) {
        this.popUpBestActionMessage = popUpBestActionMessage;
    }

    public String getPopUpBestActionMessage() {
        return popUpBestActionMessage;
    }

    public void setUpdateButtonBind(RichCommandButton updateButtonBind) {
        this.updateButtonBind = updateButtonBind;
    }

    public RichCommandButton getUpdateButtonBind() {
        return updateButtonBind;
    }

    public void setBestActionMessageBind(RichOutputText bestActionMessageBind) {
        this.bestActionMessageBind = bestActionMessageBind;
    }

    public RichOutputText getBestActionMessageBind() {
        return bestActionMessageBind;
    }

    public void assignmentCategoryValueChangeListener(ValueChangeEvent valueChangeEvent) {
        int value = 0;
        oracle.jbo.domain.Number num = new oracle.jbo.domain.Number(value);

        System.out.println("assignmentCategoryValueChangeListener...Bank");
        UIUtil.setValueToEL("#{bindings.EmploymentCategory.inputValue}",
                            valueChangeEvent.getNewValue());
        String asgCategoryValue =
            (String)UIUtil.resolveExpression("#{bindings.EmploymentCategory.attributeValue}");
        System.out.println("+++++ nhsOrgId Selected : " + asgCategoryValue);

        DCBindingContainer bindings = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItrBindings = bindings.findIteratorBinding("AssignmentSearchVO1Iterator");
        ViewObject voTabledata = dcItrBindings.getViewObject();
        Row selectedRow = voTabledata.getCurrentRow();

        if (asgCategoryValue != null && asgCategoryValue.trim().length() > 0) {
            if (asgCategoryValue.equalsIgnoreCase("B")) {
                System.out.println("BankBank...Bank");
                UIUtil.setValueToEL("#{bindings.WorkingHours.inputValue}", num);
                errorMessage ="Working Hours have been set to zero for Assignment Category of Bank.";
                //=====================================================================================
                 Object frequency="W";
                selectedRow.setAttribute("Frequency", (Object)frequency);
                //===================================================================================     
                workingHoursBind.setDisabled(true);
                UIUtil.showPopUp(errorPopup);
            } else {
                //UIUtil.setValueToEL("#{bindings.WorkingHours.inputValue}",null);WORKING_HOURS
                selectedRow.setAttribute("WorkingHours", null);
                selectedRow.setAttribute("Frequency", null);
                workingHoursBind.setDisabled(false);
            }
        }
    }

    public void setNhsPeopleGroupKFFPopup(RichPopup nhsPeopleGroupKFFPopup) {
        this.nhsPeopleGroupKFFPopup = nhsPeopleGroupKFFPopup;
    }

    public RichPopup getNhsPeopleGroupKFFPopup() {
        return nhsPeopleGroupKFFPopup;
    }

    public void setGroupKFFInptTxtBind(RichInputText groupKFFInptTxtBind) {
        this.groupKFFInptTxtBind = groupKFFInptTxtBind;
    }

    public RichInputText getGroupKFFInptTxtBind() {
        return groupKFFInptTxtBind;
    }

    public void setPayPointLOVBind(RichSelectOneChoice payPointLOVBind) {
        this.payPointLOVBind = payPointLOVBind;
    }

    public RichSelectOneChoice getPayPointLOVBind() {
        return payPointLOVBind;
    }

    public void setPeopleGroupId(BigDecimal peopleGroupId) {
        this.peopleGroupId = peopleGroupId;
    }

    public BigDecimal getPeopleGroupId() {
        return peopleGroupId;
    }


    // The below methods are added by Priya----------------------------------------------------------------------------------------------------------

    public void jobChangeListener(ValueChangeEvent valueChangeEvent) {

        System.out.println("Job value change listener");
        if (valueChangeEvent.getNewValue() != null) {
            String newJobValue = (String)valueChangeEvent.getNewValue();
            newJobValue = newJobValue.trim();
            if (newJobValue.isEmpty()) {
                System.out.println("empty job");
                DCBindingContainer dcBindings =
                    (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
                DCIteratorBinding iterBind =
                    (DCIteratorBinding)dcBindings.get("AssignmentSearchVO1Iterator");
                Row currentAssignment = iterBind.getCurrentRow();
                
                Object orgName=currentAssignment.getAttribute("OrganizationName");
                Object orgId=currentAssignment.getAttribute("OrganizationId");
                
                currentAssignment.setAttribute("JobId", null);
                currentAssignment.setAttribute("JobName", null);
                currentAssignment.setAttribute("PositionId", null);
                currentAssignment.setAttribute("PositionName", null);
                
                currentAssignment.setAttribute("OrganizationId",orgId);
                currentAssignment.setAttribute("OrganizationName",orgName);
            }
        }
    }


    public void organizationChangeListener(ValueChangeEvent valueChangeEvent) {
      
    try{  
        System.out.println("org value change listener");
        String orgNewValue =
            valueChangeEvent.getNewValue() != null ? (String)valueChangeEvent.getNewValue() :
            null;
        String orgOldValue =
            valueChangeEvent.getOldValue() != null ? (String)valueChangeEvent.getOldValue() :
            null;
        //If organization value is changed Position value should be removed
        if (!orgNewValue.equals(orgOldValue)) {
            System.out.println("organization changed");
            DCBindingContainer dcBindings =
                (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
            DCIteratorBinding iterBind =
                (DCIteratorBinding)dcBindings.get("AssignmentSearchVO1Iterator");
            Row currentAssignment = iterBind.getCurrentRow();
            currentAssignment.setAttribute("PositionId", null);
            currentAssignment.setAttribute("PositionName", null);

            if (currentAssignment.getAttribute("LocationId") == null &&
                currentAssignment.getAttribute("LocationCode") == null) {
                locationConfirmationPopup.show(new RichPopup.PopupHints());
            }
        }
     }catch(Exception e){
                   e.printStackTrace();
               }
    }

    public void setLocationConfirmationPopup(RichPopup locationConfirmationPopup) {
        this.locationConfirmationPopup = locationConfirmationPopup;
    }

    public RichPopup getLocationConfirmationPopup() {
        return locationConfirmationPopup;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public Date getCurrentDate() {
        return new Date();
    }


    public void locationConfirmationDialogListener(DialogEvent dialogEvent) {
        // Add event code here...
        Outcome outcome = dialogEvent.getOutcome();
        if (outcome == Outcome.yes) {
            System.out.println("location confirmation yes");
            DCBindingContainer dcBindings =
                (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
            DCIteratorBinding iterBind =
                (DCIteratorBinding)dcBindings.get("AssignmentSearchVO1Iterator");
            Row currentAssignment = iterBind.getCurrentRow();
            Object orgId = currentAssignment.getAttribute("OrganizationId");
            oracle.jbo.domain.Number organizationId =
                orgId != null ? (oracle.jbo.domain.Number)orgId : null;
            NHS_AssignmentAppModuleImpl assignmentAppModule =
                (NHS_AssignmentAppModuleImpl)ADFUtil.getDataControl("NHS_AssignmentAppModuleDataControl");
            if (organizationId != null) {
                //System.out.println("organization id----"+organizationId);
                Key orgKey = new Key(new Object[] { organizationId });
                Row[] orgNames =
                    assignmentAppModule.getOrganizationsLOVVO1().findByKey(orgKey,
                                                                           1);

                if (orgNames != null && orgNames.length > 0) {
                    // System.out.println("organization id----"+organizationId);
                    OrganizationsLOVVORowImpl organization =
                        (OrganizationsLOVVORowImpl)orgNames[0];
                    oracle.jbo.domain.Number locationId =
                        organization.getLocationId();
                    String location = organization.getLocation();
                    currentAssignment.setAttribute("LocationId", locationId);
                    currentAssignment.setAttribute("LocationCode", location);
                }
            }
        }
    }
    //The above code are added by Priya

    public void setFutureChangesPresentMessage(boolean futureChangesPresentMessage) {
        this.futureChangesPresentMessage = futureChangesPresentMessage;
    }

    public boolean isFutureChangesPresentMessage() {
        return futureChangesPresentMessage;
    }

    //====================for position default value set=======================================================

    public void positionChangeListener(ValueChangeEvent valueChangeEvent) {

        //System.out.println(" in.........positionChangeListener...");
        UIUtil.showPopUp(defaultValuePositionPopup);
       // System.out.println("positionBind......" + positionBind.getValue().toString());
    }

    public void defaultValueSetForPosition(ActionEvent actionEvent) {

        DCBindingContainer bindings =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItrBindings =
            bindings.findIteratorBinding("AssignmentSearchVO1Iterator");
        ViewObject voTabledata = dcItrBindings.getViewObject();
        Row selectedRow = voTabledata.getCurrentRow();
        ViewObjectImpl voImpl = (ViewObjectImpl)dcItrBindings.getViewObject();
        //=================================================================================================
        String positionName =null;
        if( positionBind.getValue() !=null) {
        positionName = positionBind.getValue().toString();
        System.out.println("defaultValueSetForPosition......" + positionName);
        }
        
        NHS_AssignmentAppModuleImpl appmodule =
            (NHS_AssignmentAppModuleImpl)ADFUtil.getDataControl("NHS_AssignmentAppModuleDataControl");

        if (positionName != null && positionName.trim().length() > 0) {
            ViewObject defaultpositionVO =
                appmodule.getDefaultValueSetPosition1();
            defaultpositionVO.setWhereClause(" position_name = '" +
                                             positionName + "'");
            defaultpositionVO.executeQuery();

            // =============for garde===================================================
            Object selected_grade_id =
                defaultpositionVO.first().getAttribute("EntryGradeId");
            Object selected_grade_name =
                defaultpositionVO.first().getAttribute("GradeName");
            selectedRow.setAttribute("GradeId", selected_grade_id);
            selectedRow.setAttribute("GradeName", selected_grade_id);
            System.out.println("selected_grade_id..." + selected_grade_id);
            gradeNameLovBind.setValue(selected_grade_name);
            AdfFacesContext.getCurrentInstance().addPartialTarget(gradeNameLovBind);
            // =========================================================================
            // ===============================pay roll==========================================
            Object selected_payroll_id =
                defaultpositionVO.first().getAttribute("PayFreqPayrollId");
            Object selected_payroll_name =
                defaultpositionVO.first().getAttribute("PayrollName");
            selectedRow.setAttribute("PayrollId", selected_payroll_id);
            selectedRow.setAttribute("PayrollName", selected_payroll_name);
            System.out.println("selected_payroll_name..." +
                               selected_payroll_name);
            // payrollLovforPositionBind.setValue(selected_payroll_name);
            selectedRow.setAttribute("PayrollId", selected_payroll_id);
            selectedRow.setAttribute("PayrollName", selected_payroll_name);
            // payrollLovforPositionBind.setValue(1);
            AdfFacesContext.getCurrentInstance().addPartialTarget(payrollLovforPositionBind);
            // =========================================================================
            // ===============================probation period ==========================================
            Object selected_probation_period =
                defaultpositionVO.first().getAttribute("ProbationPeriod");
            selectedRow.setAttribute("ProbationPeriod",
                                     selected_probation_period);
            System.out.println("selected_probation_period..." +
                               selected_probation_period);
            // =========================================================================
            // ===============================probation period unit==========================================PROBATION_PERIOD_UNIT_CD
            Object selected_probation_period_unit =
                defaultpositionVO.first().getAttribute("ProbationPeriodUnitCd");
            selectedRow.setAttribute("ProbationUnit",
                                     selected_probation_period_unit);
            System.out.println("selected_probation_period...unit..." +
                               selected_probation_period_unit);
            // =========================================================================================================
            // ===============================BARGAINING_UNIT_CODE==========================================
            //             Object selected_bargaining_unit = defaultpositionVO.first().getAttribute("BargainingUnitCd");
            //           selectedRow.setAttribute("BargainingUnitCode",selected_probation_period);
            //             System.out.println("selected_bargaining_unit..."+selected_bargaining_unit);
            // =========================================================================
            //====================need to add salary here============================================
            // ===============================Pay Basis==========================================
            Object selected_pay_basis_unit =
                defaultpositionVO.first().getAttribute("PayBasisId");
            selectedRow.setAttribute("PayBasisId", selected_pay_basis_unit);
            System.out.println("selected_pay_basis_unit..." +
                               selected_pay_basis_unit);
            // =========================================================================
            // ===============================supervisor id set==========================================
            Object selected_supervisorId =
                defaultpositionVO.first().getAttribute("SupervisorId");
            selectedRow.setAttribute("SupervisorId", selected_supervisorId);
            System.out.println("selected_supervisorId..." +
                               selected_supervisorId);
            // =========================================================================

            //defaultValuePositionPopup.cancel();
            UIUtil.showPopUp(standardConditionForPositionPopup);
        } else {
            // defaultValuePositionPopup.cancel();
            UIUtil.showPopUp(standardConditionForPositionPopup);
        }
    }

    public void defaultValueClosePopupForPosition(ActionEvent actionEvent) {
        System.out.println("... close popup");
        defaultValuePositionPopup.cancel();
        UIUtil.showPopUp(standardConditionForPositionPopup);

    }


    public void standardConditionsPositionDialogueListener(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome() == DialogEvent.Outcome.yes) {

            DCBindingContainer bindings =
                (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
            DCIteratorBinding dcItrBindings =
                bindings.findIteratorBinding("AssignmentSearchVO1Iterator");
            ViewObject voTabledata = dcItrBindings.getViewObject();
            Row selectedRow = voTabledata.getCurrentRow();
            ViewObjectImpl voImpl =
                (ViewObjectImpl)dcItrBindings.getViewObject();
            //=================================================================================================

            NHS_AssignmentAppModuleImpl appmodule =
                (NHS_AssignmentAppModuleImpl)ADFUtil.getDataControl("NHS_AssignmentAppModuleDataControl");
            ViewObject defaultpositionVO =
                appmodule.getDefaultValueSetPosition1();
            // ===============================org name and id==========================================
            String selected_org_name =
                ((String)defaultpositionVO.first().getAttribute("OrganizationName"));
            System.out.println("... selected_org_name..." + selected_org_name);
            if (selected_org_name != null &&
                selected_org_name.trim().length() > 0) {
                organizationNameBind.setValue(selected_org_name);
                AdfFacesContext.getCurrentInstance().addPartialTarget(organizationNameBind);
            }
            Object selected_org_id =
                defaultpositionVO.first().getAttribute("OrganizationId");
            selectedRow.setAttribute("OrganizationId", selected_org_id);
            System.out.println("after 2nd selected_org_id..." +
                               selected_org_id);
            //
            //=================job name===============================================================
            String selected_job_name =
                ((String)defaultpositionVO.first().getAttribute("JobName"));
            System.out.println("... selected_job_name..." + selected_job_name);
            if (selected_job_name != null &&
                selected_job_name.trim().length() > 0) {
                jobNameBind.setValue(selected_job_name);
                AdfFacesContext.getCurrentInstance().addPartialTarget(jobNameBind);
            }
            Object selected_job_id =
                defaultpositionVO.first().getAttribute("JobId");
            selectedRow.setAttribute("JobId", selected_job_id);
            System.out.println("after 2nd selected_job_id..." +
                               selected_job_id);

            // need to set all those value of standard conditions , below
            //hap.working_hours,hap.frequency,hap.TIME_NORMAL_FINISH,hap.TIME_NORMAL_START
            //====================WORKING_HOURS====================================
            BigDecimal selected_working_hours =
                ((BigDecimal)defaultpositionVO.first().getAttribute("WorkingHours"));
            System.out.println("... selected_working_hours..." +
                               selected_working_hours);
            float working_hours = selected_working_hours.floatValue();
            oracle.jbo.domain.Number working_hours_domain =
                new oracle.jbo.domain.Number(working_hours);
            UIUtil.setValueToEL("#{bindings.WorkingHours.inputValue}", working_hours_domain);

            selectedRow.setAttribute("WorkingHours",  defaultpositionVO.first().getAttribute("WorkingHours"));
            System.out.println("....WorkingHours  get...." +selectedRow.getAttribute("WorkingHours"));


            String selected_FREQUENCY =  ((String)defaultpositionVO.first().getAttribute("Frequency"));
            System.out.println("... selected_FREQUENCY..." + selected_FREQUENCY);
            if (selected_FREQUENCY != null)
                selectedRow.setAttribute("Frequency",  defaultpositionVO.first().getAttribute("Frequency"));
            System.out.println("....Frequency  get...." +selectedRow.getAttribute("Frequency"));


            String selected_TIME_NORMAL_START = ((String)defaultpositionVO.first().getAttribute("TimeNormalStart"));
            System.out.println("... selected_TIME_NORMAL_START..." + selected_TIME_NORMAL_START);
            if (selected_TIME_NORMAL_START != null && selected_job_name.trim().length() > 0) {
                UIUtil.setValueToEL("#{bindings.TimeNormalStart.inputValue}",
                                    selected_TIME_NORMAL_START);

                selectedRow.setAttribute("TimeNormalStart", defaultpositionVO.first().getAttribute("TimeNormalStart"));
                System.out.println("....TimeNormalStart  get...." + selectedRow.getAttribute("TimeNormalStart"));

            }

            String selected_TimeNormalFinish =  ((String)defaultpositionVO.first().getAttribute("TimeNormalFinish"));
            System.out.println("... selected_TimeNormalFinish..." +selected_TimeNormalFinish);
            if (selected_TIME_NORMAL_START != null && selected_job_name.trim().length() > 0) {
                UIUtil.setValueToEL("#{bindings.TimeNormalFinish.inputValue}",selected_TimeNormalFinish);

                selectedRow.setAttribute("TimeNormalFinish",  defaultpositionVO.first().getAttribute("TimeNormalFinish"));
                System.out.println("....TimeNormalFinish  get...." +selectedRow.getAttribute("TimeNormalFinish"));

            }
            
            //===========================================================EMP_CATEGORY_MEANING==============================
//            if(selected_FREQUENCY !=null & selected_working_hours !=null){
//                    Object empCategory="Full Time";
//                    selectedRow.setAttribute("EmpCategoryMeaning", (Object)empCategory);
//                    Object empCategoryCode="FT";
//                    selectedRow.setAttribute("EmpCategoryMeaning", (Object)empCategoryCode);
//                }
//                        
            //=================================================================================

            UIUtil.showPopUp(locationForPositioinPopupBind);
        }


        if (dialogEvent.getOutcome() == DialogEvent.Outcome.no) {
            System.out.println("..in no standardConditionsPositionDialogueListener...");
            UIUtil.showPopUp(locationForPositioinPopupBind);
        }


    }

    public void locationForPositionDialogueListener(DialogEvent dialogEvent) {
        //=================================================================================================
        NHS_AssignmentAppModuleImpl appmodule =
            (NHS_AssignmentAppModuleImpl)ADFUtil.getDataControl("NHS_AssignmentAppModuleDataControl");
        ViewObject defaultpositionVO = appmodule.getDefaultValueSetPosition1();
        //=================================================================================================
        String selected_subjectiveCode =
            ((String)defaultpositionVO.first().getAttribute("CostingInformation"));
        String selected_location_name =
            ((String)defaultpositionVO.first().getAttribute("LocationName"));


        //======================================================== 
        FacesContext fctx = FacesContext.getCurrentInstance();
        ExternalContext ectx = fctx.getExternalContext();
        Map<String, Object> scopeMap = ectx.getSessionMap();
        scopeMap.put("subjectiveCode", selected_subjectiveCode);
        HttpSession httpsession = (HttpSession)ectx.getSession(true);
        httpsession.setAttribute("subjectiveCode",selected_subjectiveCode);
        //========================================================
        
        if (dialogEvent.getOutcome() == DialogEvent.Outcome.yes) {

            DCBindingContainer bindings =
                (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
            DCIteratorBinding dcItrBindings =
                bindings.findIteratorBinding("AssignmentSearchVO1Iterator");
            ViewObject voTabledata = dcItrBindings.getViewObject();
            Row selectedRow = voTabledata.getCurrentRow();
            ViewObjectImpl voImpl =
                (ViewObjectImpl)dcItrBindings.getViewObject();

            System.out.println("... selected_location_name..." +
                               selected_location_name);
            if (selected_location_name != null &&
                selected_location_name.trim().length() > 0) {
                locationNameBind.setValue(selected_location_name);
                AdfFacesContext.getCurrentInstance().addPartialTarget(locationNameBind);
            }

            Object selected_location_id =
                defaultpositionVO.first().getAttribute("LocationId");
            selectedRow.setAttribute("LocationId", selected_location_id);
            selectedRow.setAttribute("CostingInformation",
                                     selected_subjectiveCode);
            selectedRow.setAttribute("LocationCode", selected_location_name);

            System.out.println("after 2nd selected_location_id..." +
                               selected_subjectiveCode);

//            errorMessage =
//                    "Costing Information has been updated for this Assignment.The Subjective Code will be set to " +
//                    selected_subjectiveCode +
//                    " if similar matches found. Please navigate to the Costing form to review this.";
//            UIUtil.showPopUp(errorPopup);
            // locationForPositioinPopupBind.cancel();
        }
        if (dialogEvent.getOutcome() == DialogEvent.Outcome.no) {
//            errorMessage =
//                    "Costing Information has been updated for this Assignment.The Subjective Code will be set to " +
//                    selected_subjectiveCode +
//                    " if similar matches found. Please navigate to the Costing form to review this.";
//            UIUtil.showPopUp(errorPopup);
      }

    }

    public void changeReasonValidationChangeListener(ValueChangeEvent valueChangeEvent) {
       
        if (valueChangeEvent.getOldValue() != valueChangeEvent.getNewValue()) {
                     changeReasonFlag=true;
//                errorMessage ="A change has been made to the person's assignment details, ensure that an appropriate assignment change reason has been selected.";
//                UIUtil.showPopUp(errorPopup);
        
        }

    }

    public void ChangeReasonYesButtonPressed(ActionEvent actionEvent) {
        // donot call api
        System.out.println("donot call api");
    }

    public void ChangeReasonNoPressed(ActionEvent actionEvent) {
        // call api , callig method
        System.out.println("call api , callig method");
    }

    public void assignmentStatusChangeListener(ValueChangeEvent valueChangeEvent) {
        DCBindingContainer bindings =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItrBindings =
            bindings.findIteratorBinding("AssignmentSearchVO1Iterator");
        ViewObject voTabledata = dcItrBindings.getViewObject();
        Row selectedRow = voTabledata.getCurrentRow();
        ViewObjectImpl voImpl = (ViewObjectImpl)dcItrBindings.getViewObject();


        UIUtil.setValueToEL("#{bindings.UserStatus.inputValue}",
                            valueChangeEvent.getNewValue());
        String UserStatus =
            (String)UIUtil.resolveExpression("#{bindings.UserStatus.attributeValue}");
        System.out.println("+++++ UserStatus Selected : " + UserStatus);
        setAssignmentStatus(UserStatus);
        System.out.println("+++++getAssignmentStatus..." +
                           getAssignmentStatus());
        //==============================================================================================================
        String primaryFlag = null;
        if ((selectedRow.getAttribute("PrimaryFlag")) != null) {
            primaryFlag = (selectedRow.getAttribute("PrimaryFlag")).toString();
            System.out.println("....primaryFlag..." + primaryFlag);
        } else {
            // String primaryFlag = (String)selectedRow.getAttribute("PrimaryFlag");
            selectedRow.setAttribute("PrimaryFlag", "N");
            primaryFlag = "N";
            System.out.println("....primaryFlag..else" +
                               selectedRow.getAttribute("PrimaryFlag"));
        }
        String assgnStatus = assignmentStatusLOVBind.getValue().toString();
        System.out.println("....assignmentStatusLOVBind..." +
                           assignmentStatusLOVBind.getValue().toString());
        System.out.println("....valueChangeEvent.." +
                           valueChangeEvent.getNewValue());


        System.out.println(" ...update assignment....");

        if (UserStatus != null) {
            if (UserStatus.equalsIgnoreCase("Terminate Assignment") || UserStatus.equalsIgnoreCase("End")) {
                if (primaryFlag.equalsIgnoreCase("Y")) {
                    String status = "Active Assignment";
                    //  UIUtil.setValueToEL("#{bindings.UserStatus.inputValue}", 1);
                    assignmentStatusLOVBind.setValue(1);
                    AdfFacesContext adfFacesContext =
                        AdfFacesContext.getCurrentInstance();
                    adfFacesContext.addPartialTarget(assignmentStatusLOVBind);
                    errorMessage =
                            "Cannot terminate or end a primary assignment - status will be reset. ";
                    UIUtil.showPopUp(errorPopup);
                }

                else {
                    if(UserStatus.equalsIgnoreCase( "Active Assignment")){
                            errorMessage =
                                    "You cannot correct the first assignment status. You attempted to change the " +
                                    "assignment status something other than the active. Only active assignment statuses are" +
                                    "  permissible for the first occurance of each assignment.";
                            UIUtil.showPopUp(errorPopup);
                        }
                }
            }
        }

        else {
            errorMessage =
                    "Assignment Status is a mandatory field. Please choose a value from dropdown. ";
            UIUtil.showPopUp(errorPopup);
        }


    }

    public void gradeValidationForPositionChangeListener(ValueChangeEvent valueChangeEvent) {
        // gradeNameLovBind
        String newValue =
            valueChangeEvent.getNewValue() != null ? ((String)valueChangeEvent.getNewValue()).trim() :
            null;
        String oldValue =
                    valueChangeEvent.getOldValue() != null ? ((String)valueChangeEvent.getOldValue()).trim() : null;
        if (newValue == null || newValue.isEmpty()) {
            //need to add one and condition to grade step palcement is null or not
            if (isGradeStepEntered()) {
                //if(gradeNameLovBind.getValue() ==null && grade step palcement binding not = null)
                errorMessage =
                        "Grade Step Placements are defined against this assignment Please remove the grade step information manually using Grade Step Placement window before removing the grade of the assignment.";
                //UIUtil.showPopUp(errorPopup);
                ADFUtil.showMessage(errorMessage, FacesMessage.SEVERITY_ERROR);
                return;
            }
            //if no grade step is defined grade id need to be set to null
            else {
                DCBindingContainer bindings =
                    (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
                DCIteratorBinding iterBind =
                    (DCIteratorBinding)bindings.get("AssignmentSearchVO1Iterator");
                Row currentAssignment = iterBind.getCurrentRow();
                currentAssignment.setAttribute("GradeId", null);

            }
        }
        if(!newValue.equals(oldValue)) {
                    Map sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
                    Boolean gradeUpdated = sessionMap.get("isGradeUpdated") != null ? (Boolean)sessionMap.get("isGradeUpdated") : false; 
                    String gradeOldValue = sessionMap.get("gradeOldValue") != null ? (String)sessionMap.get("gradeOldValue") : null;
                    if(gradeOldValue == null) {
                        gradeOldValue = oldValue;
                        sessionMap.put("gradeOldValue", gradeOldValue);
                    }
                    //grade is updated
                    if(!newValue.equals(gradeOldValue))
                    {
                        System.out.println("grade is updated");
                        sessionMap.put("isGradeUpdated", true);
                    }
                    else
                    {
                        System.out.println("grade is not updated");
                        sessionMap.put("isGradeUpdated", false);
                    }
                    
        }
        NHS_AssignmentAppModuleImpl appmodule =
            (NHS_AssignmentAppModuleImpl)ADFUtil.getDataControl("NHS_AssignmentAppModuleDataControl");
        System.out.println("positionBind......" + positionBind.getValue());
        System.out.println("gradeNameLovBind......" +
                           gradeNameLovBind.getValue());
        //String positionName= positionBind.getValue().toString();
        //String gradename= gradeNameLovBind.getValue().toString();
        //System.out.println("position name......"+positionName);
        //System.out.println("gradename name......"+gradename);


        if (positionBind.getValue() != null &&
            gradeNameLovBind.getValue() != null) {

            String positionName = positionBind.getValue().toString();
            String gradename = gradeNameLovBind.getValue().toString();

            if (positionName != null && positionName.trim().length() > 0) {
                ViewObject defaultpositionVO =
                    appmodule.getDefaultValueSetPosition1();
                defaultpositionVO.setWhereClause(" position_name = '" +
                                                 positionName + "'");
                defaultpositionVO.executeQuery();
                String selected_grade_name =
                    defaultpositionVO.first().getAttribute("GradeName").toString();
                System.out.println(" selected_grade_name...." +
                                   selected_grade_name);

                if (!selected_grade_name.equalsIgnoreCase(gradename)) {
                    errorMessage =
                            "The grade you are entering for this assignment is different from the entry grade of the position.";
                    UIUtil.showPopUp(errorPopup);
                }


                if (gradename != null && gradename.trim().length() > 0) {
                    //  first need to set grade step palcement input field as disabled
                    // set grade step palcement input field as enable and progammatically ppr, refresh the component
                    System.out.println(" set grade step palcement input field as enable ");
                } else {
                    // set grade step palcement input field as disabled and progammatically ppr, refresh the component
                    System.out.println(" set grade step palcement input field as disable ");
                }

            }
        }

    }


    public void supervisorValueChangeListener(ValueChangeEvent valueChangeEvent) {
        //        NHS_AssignmentAppModuleImpl appmodule =   (NHS_AssignmentAppModuleImpl)ADFUtil.getDataControl("NHS_AssignmentAppModuleDataControl");
        supervisorIsValid=true;
        DCBindingContainer bindings =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItrBindings =
            bindings.findIteratorBinding("AssignmentSearchVO1Iterator");
        ViewObject voTabledata = dcItrBindings.getViewObject();
        Row selectedRow = voTabledata.getCurrentRow();
        ViewObjectImpl voImpl = (ViewObjectImpl)dcItrBindings.getViewObject();
        selectedRow.setAttribute("SupervisorAssignmentNumber", " ");
        
        System.out.println("...supervisorEmployeeNumberBind...." +
                           supervisorEmployeeNumberBind.getValue());
        System.out.println("...supervisorNameBind...." +
                           supervisorNameBind.getValue());
        // String supervisorId=((Number)selectedRow.getAttribute("SupervisorId")).toString();
        //  Object supervisorId=selectedRow.getAttribute("SupervisorId");
        System.out.println("...selectedRow.." +
                           selectedRow.getAttribute("SupervisorName"));
        String fullName = null;
        if (supervisorNameBind.getValue() != null) {
            fullName = supervisorNameBind.getValue().toString();
        }
        //20000128
        if (fullName != null) {

            System.out.println("......supervisorId...." + fullName);
            NHS_AssignmentAppModuleImpl appmodule =
                (NHS_AssignmentAppModuleImpl)ADFUtil.getDataControl("NHS_AssignmentAppModuleDataControl");

            ViewObject peopleVO = appmodule.getPeopleVO1();
            peopleVO.setWhereClause(" full_name = '" + fullName + "'");
            peopleVO.executeQuery();
            if (peopleVO.getEstimatedRowCount() > 0) {
                Date supervisor_start_date =
                    ((Date)peopleVO.first().getAttribute("EffectiveStartDate"));
                System.out.println("...supervisor_start_date..." +
                                   supervisor_start_date);

                String personId =
                    ((Number)selectedRow.getAttribute("PersonId")).toString();
                peopleVO.setWhereClause(null);
                peopleVO.setWhereClause(" person_id = '" + personId + "'");
                peopleVO.executeQuery();
                Date employee_start_date =
                    ((Date)peopleVO.first().getAttribute("EffectiveStartDate"));

                System.out.println("...employee_start_date..." +
                                   employee_start_date);
                if (employee_start_date.before(supervisor_start_date)) {
                    System.out.println("...in if after....");
                    supervisorIsValid=false;
                    errorMessage =
                            "Supervisor is not valid for the duration of the assignment.";
                    UIUtil.showPopUp(errorPopup);
                }
            }
        }
        String oldValue = valueChangeEvent.getOldValue() != null ? (String)valueChangeEvent.getOldValue() : null;
        String newValue = valueChangeEvent.getNewValue() != null ? (String)valueChangeEvent.getNewValue() : null;
        if(!newValue.equals(oldValue)) {
            Row currentAssignment = dcItrBindings.getCurrentRow();
            currentAssignment.setAttribute("SupervisorAssignmentNumber", null);
        }
    }

    //supervisorEmployeeNumberValueChangeListener

    public void supervisorEmployeeNumberValueChangeListener(ValueChangeEvent valueChangeEvent) {
        //        NHS_AssignmentAppModuleImpl appmodule =   (NHS_AssignmentAppModuleImpl)ADFUtil.getDataControl("NHS_AssignmentAppModuleDataControl");
        supervisorIsValid=true;
        DCBindingContainer bindings =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItrBindings =
            bindings.findIteratorBinding("AssignmentSearchVO1Iterator");
        ViewObject voTabledata = dcItrBindings.getViewObject();
        Row selectedRow = voTabledata.getCurrentRow();
        ViewObjectImpl voImpl = (ViewObjectImpl)dcItrBindings.getViewObject();
        selectedRow.setAttribute("SupervisorAssignmentNumber", " ");
        
        System.out.println("...supervisorEmployeeNumberBind...." +
                           supervisorEmployeeNumberBind.getValue());
        System.out.println("...supervisorNameBind...." +
                           supervisorNameBind.getValue());
        // String supervisorId=((Number)selectedRow.getAttribute("SupervisorId")).toString();
        //  Object supervisorId=selectedRow.getAttribute("SupervisorId");
        System.out.println("...selectedRow.." +
                           selectedRow.getAttribute("SupervisorName"));
        String supervisorEmployeeNumber = null;
        if (supervisorEmployeeNumberBind.getValue() != null) {
            supervisorEmployeeNumber =
                    supervisorEmployeeNumberBind.getValue().toString();
        }
        //20000128
        if (supervisorEmployeeNumber != null) {

            System.out.println("......supervisorEmployeeNumber...." +
                               supervisorEmployeeNumber);
            NHS_AssignmentAppModuleImpl appmodule =
                (NHS_AssignmentAppModuleImpl)ADFUtil.getDataControl("NHS_AssignmentAppModuleDataControl");

            ViewObject peopleVO = appmodule.getPeopleVO1();
            peopleVO.setWhereClause(" employee_number = '" +
                                    supervisorEmployeeNumber + "'");
            peopleVO.executeQuery();
            if (peopleVO.getEstimatedRowCount() > 0) {
                Date supervisor_start_date =
                    ((Date)peopleVO.first().getAttribute("EffectiveStartDate"));
                System.out.println("...supervisor_start_date..." +
                                   supervisor_start_date);

                String personId =
                    ((Number)selectedRow.getAttribute("PersonId")).toString();
                peopleVO.setWhereClause(null);
                peopleVO.setWhereClause(" person_id = '" + personId + "'");
                peopleVO.executeQuery();
                Date employee_start_date =
                    ((Date)peopleVO.first().getAttribute("EffectiveStartDate"));

                System.out.println("...employee_start_date..." +
                                   employee_start_date);
                if (employee_start_date.before(supervisor_start_date)) {
                    System.out.println("...in if after....");
                    supervisorIsValid=false;
                    errorMessage =
                            "Supervisor is not valid for the duration of the assignment.";
                    UIUtil.showPopUp(errorPopup);
                }
            }
        }
    }

    public void setPositionBind(RichInputListOfValues positionBind) {
        this.positionBind = positionBind;
    }

    public RichInputListOfValues getPositionBind() {
        return positionBind;
    }

    public void setDefaultValuePositionPopup(RichPopup defaultValuePositionPopup) {
        this.defaultValuePositionPopup = defaultValuePositionPopup;
    }

    public RichPopup getDefaultValuePositionPopup() {
        return defaultValuePositionPopup;
    }

    public void setGradeNameLovBind(RichInputListOfValues gradeNameLovBind) {
        this.gradeNameLovBind = gradeNameLovBind;
    }

    public RichInputListOfValues getGradeNameLovBind() {
        return gradeNameLovBind;
    }

    public void setPayrollLovforPositionBind(RichSelectOneChoice payrollLovforPositionBind) {
        this.payrollLovforPositionBind = payrollLovforPositionBind;
    }

    public RichSelectOneChoice getPayrollLovforPositionBind() {
        return payrollLovforPositionBind;
    }

    public void setStandardConditionForPositionPopup(RichPopup standardConditionForPositionPopup) {
        this.standardConditionForPositionPopup =
                standardConditionForPositionPopup;
    }

    public RichPopup getStandardConditionForPositionPopup() {
        return standardConditionForPositionPopup;
    }

    public void setOrganizationNameBind(RichInputListOfValues organizationNameBind) {
        this.organizationNameBind = organizationNameBind;
    }

    public RichInputListOfValues getOrganizationNameBind() {
        return organizationNameBind;
    }

    public void setJobNameBind(RichInputListOfValues jobNameBind) {
        this.jobNameBind = jobNameBind;
    }

    public RichInputListOfValues getJobNameBind() {
        return jobNameBind;
    }

    public void setLocationForPositioinPopupBind(RichPopup locationForPositioinPopupBind) {
        this.locationForPositioinPopupBind = locationForPositioinPopupBind;
    }

    public RichPopup getLocationForPositioinPopupBind() {
        return locationForPositioinPopupBind;
    }

    public void setLocationNameBind(RichInputListOfValues locationNameBind) {
        this.locationNameBind = locationNameBind;
    }

    public RichInputListOfValues getLocationNameBind() {
        return locationNameBind;
    }

   

    public void setAssignmentStatusLOVBind(RichSelectOneChoice assignmentStatusLOVBind) {
        this.assignmentStatusLOVBind = assignmentStatusLOVBind;
    }

    public RichSelectOneChoice getAssignmentStatusLOVBind() {
        return assignmentStatusLOVBind;
    }


    public void setSupervisorNameBind(RichInputListOfValues supervisorNameBind) {
        this.supervisorNameBind = supervisorNameBind;
    }

    public RichInputListOfValues getSupervisorNameBind() {
        return supervisorNameBind;
    }

    public void setSupervisorEmployeeNumberBind(RichInputListOfValues supervisorEmployeeNumberBind) {
        this.supervisorEmployeeNumberBind = supervisorEmployeeNumberBind;
    }

    public RichInputListOfValues getSupervisorEmployeeNumberBind() {
        return supervisorEmployeeNumberBind;
    }

    public void setShowDetailItemIsDisable(boolean showDetailItemIsDisable) {
        this.showDetailItemIsDisable = showDetailItemIsDisable;
    }

    public boolean isShowDetailItemIsDisable() {
        return showDetailItemIsDisable;
    }

    public void setAssignmentStatus(String AssignmentStatus) {
        this.AssignmentStatus = AssignmentStatus;
    }

    public String getAssignmentStatus() {
        return AssignmentStatus;
    }


    public void setPrimaryFlagIsDisable(boolean primaryFlagIsDisable) {
        this.primaryFlagIsDisable = primaryFlagIsDisable;
    }

    public boolean isPrimaryFlagIsDisable() {
        return primaryFlagIsDisable;
    }

    public void setStandardConditionSaveButtonIsDisable(boolean standardConditionSaveButtonIsDisable) {
        this.standardConditionSaveButtonIsDisable =
                standardConditionSaveButtonIsDisable;
    }

    public boolean isStandardConditionSaveButtonIsDisable() {
        return standardConditionSaveButtonIsDisable;
    }

    public void setUpdationModeSet(String updationModeSet) {
        this.updationModeSet = updationModeSet;
    }

    public String getUpdationModeSet() {
        return updationModeSet;
    }

    public void setUpdateAndCorreectionPopupBind(RichPopup updateAndCorreectionPopupBind) {
        this.updateAndCorreectionPopupBind = updateAndCorreectionPopupBind;
    }

    public RichPopup getUpdateAndCorreectionPopupBind() {
        return updateAndCorreectionPopupBind;
    }

    public void setReplaceAndInsertPopupBind(RichPopup replaceAndInsertPopupBind) {
        this.replaceAndInsertPopupBind = replaceAndInsertPopupBind;
    }

    public RichPopup getReplaceAndInsertPopupBind() {
        return replaceAndInsertPopupBind;
    }

    public String updateModeSetting() {
        updationModeSet = "UPDATE";
        System.out.println(".....updateModeSetting.." + updationModeSet);
        //==========logic to open replace And Insert Popup ============================================
        oracle.jbo.domain.Date assignmentEffEndDate = null;
        DCBindingContainer bindings =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItrBindings =
            bindings.findIteratorBinding("AssignmentSearchVO1Iterator");
        ViewObject voTabledata = dcItrBindings.getViewObject();
        Row selectedRow = voTabledata.getCurrentRow();
        
        //=====================================================================================================================
        //When actor selects �Update� mode for the record with effective start date same as effective date selected, 
        //system should change the mode to �Correction� and display the message as indicated below.
        //=====================================================================================================================
        java.sql.Date effectiveDatesql = null;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("p_effective_date") !=null) {
            Date effectiveDate =
                (java.util.Date)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("p_effective_date");
            effectiveDatesql = new java.sql.Date(effectiveDate.getTime());
            System.out.println("...if ... effectiveDatesql...." +
                               effectiveDatesql);
        } else {
            effectiveDatesql =
                    new java.sql.Date(new java.util.Date().getTime());
            System.out.println("...else ... effectiveDatesql...." + effectiveDatesql);
        }
        
        oracle.jbo.domain.Date jboDateEffective = new oracle.jbo.domain.Date(effectiveDatesql);
        System.out.println(".... ..jboDateEffective..."+jboDateEffective);
        
        oracle.jbo.domain.Date assignmentEffStartDateJbo = (oracle.jbo.domain.Date)selectedRow.getAttribute("AssignmentEffStartDate");
        System.out.println("....assignmentEffStartDateJbo......."+assignmentEffStartDateJbo);
                    
        if(assignmentEffStartDateJbo.equals(jboDateEffective)){
                   System.out.println("...in if....equals...assignmentEffStartDateJbo.."+updationModeSet);
                     UIUtil.showPopUp(updateModeChangerPopupBind);
                     return null;
            }
        //==============================================================================================
        //When actor selects �Update� mode on the record with �End Date�, system should prompt with the option as Replace And Insert option
        //==============================================================================================
        System.out.println("....AssignmentEffEndDate ..popup....1..."+selectedRow.getAttribute("AssignmentEffEndDate"));
//        if (selectedRow.getAttribute("AssignmentEffEndDate") != null) {
//
//            assignmentEffEndDate = (oracle.jbo.domain.Date)selectedRow.getAttribute("AssignmentEffEndDate");
//            System.out.println("....AssignmentEffEndDate ..popup....2..."+assignmentEffEndDate);
//
//            if ((assignmentEffEndDate.toString()).equalsIgnoreCase("4712-12-31")) {
//                System.out.println(".....in if assignmentEffEndDate..4712-12-31.");
//            } else {
//                UIUtil.showPopUp(replaceAndInsertPopupBind);
//                return null;
//            }
//        }
        if (futureChangesPresentMessage && payrollValueChangeListenerCalled) {
                UIUtil.showPopUp(replaceAndInsertPayrollPopupBind);
                return null;
            } else if (futureChangesPresentMessage && !payrollValueChangeListenerCalled) {
                UIUtil.showPopUp(replaceAndInsertPopupBind);
                return null;
            }
        return assignmentRecordSave();
    }

//    public void updateModeChangerDialogListener(DialogEvent dialogEvent) {
//         if (dialogEvent.getOutcome() == DialogEvent.Outcome.ok) { 
//                updationModeSet = "CORRECTION";
//                correctionModeSetting();
//            }
//    }
    
    
  public String updateModeChanger() {
              updationModeSet = "CORRECTION";
                System.out.println(".....updateModeChanger.." + updationModeSet);
                return assignmentRecordSave();
         }

    public String correctionModeSetting() {
        updationModeSet = "CORRECTION";
        System.out.println(".....correctionModeSetting.." + updationModeSet);
        return assignmentRecordSave();
    }
    
    public String replaceModeSetting() {
         updationModeSet = "UPDATE_OVERRIDE";
         System.out.println(".....correctionModeSetting.." + updationModeSet);
         return assignmentRecordSave();
    }

    public String insertModeSetting() {
        updationModeSet = "UPDATE_CHANGE_INSERT";
        System.out.println(".....correctionModeSetting.." + updationModeSet);
        return assignmentRecordSave();
    }

    public void gradeLOVPopupReturnListener(ReturnPopupEvent returnPopupEvent) {
        //access UI component instance from return event
        RichInputListOfValues lovField =
            (RichInputListOfValues)returnPopupEvent.getSource();

        //The LOVModel gives us access to the Collection Model and
        //ADF tree binding used to populate the lookup table
        ListOfValuesModel lovModel = lovField.getModel();
        org.apache.myfaces.trinidad.model.CollectionModel collectionModel =
            lovModel.getTableModel().getCollectionModel();

        //The collection model wraps an instance of the ADF
        //FacesCtrlHierBinding, which is casted to JUCtrlHierBinding
        JUCtrlHierBinding treeBinding =
            (JUCtrlHierBinding)collectionModel.getWrappedData();

        //the selected rows are defined in a RowKeySet.As the LOV table only
        //supports single selections, there is only one entry in the rks
        RowKeySet rks = (RowKeySet)returnPopupEvent.getReturnValue();

        //the ADF Faces table row key is a list. The list contains the
        //oracle.jbo.Key
        List tableRowKey = (List)rks.iterator().next();

        //get the iterator binding for the LOV lookup table binding
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding();

        //get the selected row by its JBO key
        Key key = (Key)tableRowKey.get(0);
        Row rw = dciter.findRowByKeyString(key.toStringFormat(true));
        Object gradeId = rw.getAttribute("GradeId");
        DCBindingContainer bindings =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItrBindings =
            bindings.findIteratorBinding("GradeStepPlacementVO1Iterator");
        ViewObject gradeStepView = dcItrBindings.getViewObject();
        
        GradeStepPlacementVORowImpl gradeStepPlacement = gradeStepView.getCurrentRow() != null ?
                    (GradeStepPlacementVORowImpl)gradeStepView.getCurrentRow() : (GradeStepPlacementVORowImpl)gradeStepView.first();
        
        System.out.println("Grade Id" + gradeId);
        gradeStepPlacement.getGradeStepPointLOVO1().setNamedWhereClauseParam("GradeId",
                                                                             gradeId);
        gradeStepPlacement.getGradeStepPointLOVO1().executeQuery();
        
        DCIteratorBinding assignmentSearchVOIter =
            bindings.findIteratorBinding("AssignmentSearchVO1Iterator");
        ViewObject assignmentView = assignmentSearchVOIter.getViewObject();
        AssignmentSearchVORowImpl assignment =
            (AssignmentSearchVORowImpl)assignmentView.getCurrentRow();
        assignment.getProgressionPointLOVVO1().setNamedWhereClauseParam("GradeId", gradeId);

    }

    private void validateAndInsertGradeStep() {
        DCBindingContainer bindings =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding gradeStepIterator =
            (DCIteratorBinding)bindings.get("GradeStepPlacementVO1Iterator");
        long rowsCount = gradeStepIterator.getEstimatedRowCount();
        // grade step record is not available
        if (rowsCount <= 0) {
            OperationBinding createGradeStep =
                bindings.getOperationBinding("CreateInsertGradeStep");
            createGradeStep.execute();
            System.out.println(gradeStepIterator.getEstimatedRowCount());
        }
    }

    private boolean isGradeStepEntered() {
        DCBindingContainer bindings =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding gradeStepIterator =
            (DCIteratorBinding)bindings.get("GradeStepPlacementVO1Iterator");
        Row gradeStepRow = gradeStepIterator.getCurrentRow();
        String point =
            gradeStepRow.getAttribute("Point") != null ? ((String)gradeStepRow.getAttribute("Point")).trim() :
            null;
        String reasonCode =
            gradeStepRow.getAttribute("ReasonCode") != null ? ((String)gradeStepRow.getAttribute("ReasonCode")).trim() :
            null;
        Object startDate =
            gradeStepRow.getAttribute("EffectiveStartDate") != null ?
            gradeStepRow.getAttribute("EffectiveStartDate") : null;
        java.sql.Date endDate =
            gradeStepRow.getAttribute("EffectiveEndDate") != null ?
            ((java.sql.Date)gradeStepRow.getAttribute("EffectiveEndDate")) :
            null;
        //if grade step point is defined, grade cannot be emptied
        if ((point != null && !point.isEmpty()) ||
            (reasonCode != null && !reasonCode.isEmpty()) ||
             endDate != null) {
            return true;
        }
        return false;
    }

    /**
     * If grade is not defined for assignment and grade step values are entered, return false, otherwise return true
     * @return
     */
    private boolean validateGradeStepPlacement() {
        DCBindingContainer bindings =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding iterBind =
            (DCIteratorBinding)bindings.get("AssignmentSearchVO1Iterator");
        Row currentAssignment = iterBind.getCurrentRow();
        Object gradeId = currentAssignment.getAttribute("GradeId");
        //grade is not defined for assignment and grade step values are entered
        if (gradeId == null && isGradeStepEntered()) {
            return false;
        }
        return true;
    }

    
    private boolean validateGradePeriod(){
        DCBindingContainer bindings =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding iterBind =
            (DCIteratorBinding)bindings.get("AssignmentSearchVO1Iterator");
        Row currentAssignment = iterBind.getCurrentRow();
        Object gradeId = currentAssignment.getAttribute("GradeId");
        //grade is not defined for assignment and grade step values are entered
        if(gradeId !=null && isGradeStepEntered()) {
            NHS_AssignmentAppModuleImpl appmodule =
                (NHS_AssignmentAppModuleImpl)ADFUtil.getDataControl("NHS_AssignmentAppModuleDataControl");
            Key gradeKey = new Key(new Object[] { gradeId });
            
            Row[] grades = appmodule.getGradeLOVVO1().findByKey(gradeKey, 1);
            if(grades !=null && grades.length > 0 ) {
                Row gradeRow = grades[0];
                java.sql.Date startDateOfGrade = gradeRow.getAttribute("DateFrom") != null ? (java.sql.Date)gradeRow.getAttribute("DateFrom") : null;
                DCIteratorBinding gradeStepIterator =
                    (DCIteratorBinding)bindings.get("GradeStepPlacementVO1Iterator");
                Row gradeStepRow = gradeStepIterator.getCurrentRow();
                java.sql.Date startDateOfGradeStep = gradeStepRow.getAttribute("EffectiveStartDate") != null ? (java.sql.Date)gradeStepRow.getAttribute("EffectiveStartDate") : null;
                System.out.println(startDateOfGrade.toString());
                System.out.println(startDateOfGradeStep.toString());
                if(startDateOfGrade.before(startDateOfGradeStep)) {
                    return false;
                }
            }
        }
        return true;
    }
    private boolean validateGradePeriod(java.sql.Date effectiveDate){
        DCBindingContainer bindings =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding gradeStepIterator =
            (DCIteratorBinding)bindings.get("GradeStepPlacementVO1Iterator");
        Row gradeStepRow = gradeStepIterator.getCurrentRow();
        if(gradeStepRow != null && gradeStepRow.getAttribute("PlacementId")!= null)
        {
        java.sql.Date startDateOfGradeStep = gradeStepRow.getAttribute("EffectiveStartDate") != null ? (java.sql.Date)gradeStepRow.getAttribute("EffectiveStartDate") : null;
        System.out.println(startDateOfGradeStep.toString());
        if(effectiveDate.before(startDateOfGradeStep)) {
            return false;
        }
        }
        return true;
    }
    public void peopleGroupPopupFetchListener(PopupFetchEvent popupFetchEvent) {
        DCBindingContainer bindings =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding iterBind =
            (DCIteratorBinding)bindings.get("AssignmentSearchVO1Iterator");
        Row currentAssignment = iterBind.getCurrentRow();
        Object peopleGroup = currentAssignment.getAttribute("PeopleGroupId");
        BigDecimal groupId = peopleGroup != null ? (BigDecimal)peopleGroup : null;
        if (groupId != null) {
            Key groupKey = new Key(new Object[] { groupId });
            NHS_AssignmentAppModuleImpl appmodule =
                (NHS_AssignmentAppModuleImpl)ADFUtil.getDataControl("NHS_AssignmentAppModuleDataControl");
            Row[] groupNames = appmodule.getPayPeopleGroupKFFVO1().findByKey(groupKey, 1);
            
            if (groupNames != null && groupNames.length > 0) {
                 Row group = groupNames[0];
                 if(group.getAttribute("ConcatenatedSegments")!=null) {
                  String concatenatedSegments = (String)group.getAttribute("ConcatenatedSegments");
                  String[] segments = concatenatedSegments.split("\\|");
                    if (segments.length > 0) {
                        String locationId = segments[0];
                        String locationCode = appmodule.getLocationName(new BigDecimal(locationId));
                        if(locationCode!=null) {
                            System.out.println(locationCode);
                            int index = getListValueIndex(locationCode,"PaypointLOVVO1Iterator","LocationCode");
                            payPointLOVBind.setValue(index);
                        }
                    }
                    if (segments.length > 1) {
                        String expenseUserType = segments[1];
                        System.out.println(expenseUserType);
                        int index = getListValueIndex(expenseUserType,"ExpenseUserTypeLOVVO1Iterator","FlexValueMeaning");
                        groupExpenseUserTypeLOV.setValue(index);
                    }
                    if (segments.length > 2) {
                        String timeAndAttendence = segments[2];
                        System.out.println(timeAndAttendence);
                        int index = getListValueIndex(timeAndAttendence,"TimeAndAttendanceLOVVO1Iterator","FlexValueMeaning");
                        groupTimeAndAttendanceLOV.setValue(index);
                    }
                    if (segments.length > 3) {
                        String dataEntryGroup = segments[3];
                        System.out.println(dataEntryGroup);
                        int index = getListValueIndex(dataEntryGroup,"DataEntryGroupLOVVO1Iterator","FlexValueMeaning");
                        groupDataEntryGroupLOV.setValue(index);
                    }

                }
            }
        }
    }

    public void setGroupExpenseUserTypeLOV(RichSelectOneChoice groupExpenseUserTypeLOV) {
        this.groupExpenseUserTypeLOV = groupExpenseUserTypeLOV;
    }

    public RichSelectOneChoice getGroupExpenseUserTypeLOV() {
        return groupExpenseUserTypeLOV;
    }

    
    private Integer getListValueIndex(String value, String iteratorName, String attribName) {
    int i = 0;
    BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
     
    DCIteratorBinding dciterContainer =  (DCIteratorBinding)bindings.get(iteratorName);
    long totalRowsCount = dciterContainer.getEstimatedRowCount();
    RowSetIterator rs = dciterContainer.getRowSetIterator();
    Row ri = rs.first();
    System.out.println("countttt......."+totalRowsCount);
    for(int index = 0;index < totalRowsCount; index++){
    String temp = (String)ri.getAttribute(attribName);
    if(value != null && temp.equals(value.trim())){
    i = index;
    }
    ri = rs.next();
    }
     
    return i;
    }

    public void setGroupTimeAndAttendanceLOV(RichSelectOneChoice groupTimeAndAttendanceLOV) {
        this.groupTimeAndAttendanceLOV = groupTimeAndAttendanceLOV;
    }

    public RichSelectOneChoice getGroupTimeAndAttendanceLOV() {
        return groupTimeAndAttendanceLOV;
    }

    public void setGroupDataEntryGroupLOV(RichSelectOneChoice groupDataEntryGroupLOV) {
        this.groupDataEntryGroupLOV = groupDataEntryGroupLOV;
    }

    public RichSelectOneChoice getGroupDataEntryGroupLOV() {
        return groupDataEntryGroupLOV;
    }

    //Create or update a grade step placement record - A.Roy

    public String saveGradeStepPlacementDetails() {
        //if grade is not defined and grade step is entered
        if (!validateGradeStepPlacement()) {
            ADFUtil.showMessage("You cannot enter a placement for this assignment because there is no grade scale defined for its grade",
                                FacesMessage.SEVERITY_ERROR);
        } 
        
        else {
            DCBindingContainer bindings =
                (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
            DCIteratorBinding dcItrBindings =
                (DCIteratorBinding)bindings.get("GradeStepPlacementVO1Iterator");
            GradeStepPlacementVOImpl voTabledata =
                (GradeStepPlacementVOImpl)dcItrBindings.getViewObject();
            GradeStepPlacementVORowImpl selectedGradeRow =
                (GradeStepPlacementVORowImpl)voTabledata.getCurrentRow();
            java.util.Date effectiveDate = null;
            java.sql.Date effectiveDatesql = null;
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("p_effective_date") !=
                null) {
                effectiveDate =
                        (java.util.Date)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("p_effective_date");
                System.out.println("++++ effectiveDate in if++ " +
                                   effectiveDate);
                effectiveDatesql = new java.sql.Date(effectiveDate.getTime());
                System.out.println("++++ effectiveDate in if++ " +
                                   effectiveDatesql);
            }
            placementId = (BigDecimal)selectedGradeRow.getPlacementId();
            if (placementId == null) {
                Map createGradeStepPlacementMap = new HashMap();
                if (effectiveDatesql != null){
                    createGradeStepPlacementMap.put("p_effective_date",
                                                    effectiveDatesql);
                } else {
                    createGradeStepPlacementMap.put("p_effective_date",
                                                    new java.sql.Date(new java.util.Date().getTime()));
                }
                BigDecimal assignmentId = (BigDecimal)selectedGradeRow.getAssignmentId();
                createGradeStepPlacementMap.put("p_assignment_id",
                                                assignmentId);
                createGradeStepPlacementMap.put("p_step_id",(BigDecimal)selectedGradeRow.getStepId());
                createGradeStepPlacementMap.put("p_reason",
                                                (String)selectedGradeRow.getReasonCode());
                createGradeStepPlacementMap.put("p_business_group_id", 62);
                                                
                
                System.out.println("------------------reason----"+(String)selectedGradeRow.getReasonCode());
                System.out.println("------------------step id----"+(BigDecimal)selectedGradeRow.getStep());
                System.out.println("------------------assignment id----"+assignmentId);
                Map createGradeStepPlacementReturnMap =
                    new CreateGradeStepPlacementAPICall().callCreateGradeStepPlacementAPI(createGradeStepPlacementMap);
                Set keys = createGradeStepPlacementReturnMap.keySet();
                Iterator itr = keys.iterator();
                String key = null;
                Object value = null;
                while (itr.hasNext()) {
                    key = (String)itr.next();
                    value = createGradeStepPlacementReturnMap.get(key);
                    System.out.println("-------Inside create grade step placement--------- ");
                    if ("p_error".equalsIgnoreCase(key) &&
                        (createGradeStepPlacementReturnMap.get("p_error") !=
                         null)) {
                        System.out.println("+++bean++++Error: " +
                                           createGradeStepPlacementReturnMap.get("p_error"));
                        errorMessage =
                                (String)createGradeStepPlacementReturnMap.get("p_error");
                        UIUtil.showPopUp(errorPopup);
                    } else {
                        errorMessage =
                                "Grade Step Placement created Successfully!";
                        UIUtil.showPopUp(errorPopup);
                    }
                }
            } else {
//                if (!validateGradePeriod()) {
//                    ADFUtil.showMessage("Grade step placements are defined against this assignment in the future so you are unable to change the grade of the assignment before the start date of these records.",
//                                        FacesMessage.SEVERITY_ERROR);
//                    return null;
//                }
                Map updateGradeStepPlacementMap = new HashMap();

                if (gradeModeSet == null) {
                    System.out.println("..update grade step placement flow.....updationModeSet..1.." +
                                       updationModeSet);
                    UIUtil.showPopUp(updateAndCorrectionGradePopupBind);
                } else {

                    if (effectiveDatesql != null) {
                        updateGradeStepPlacementMap.put("p_effective_date",
                                                        effectiveDatesql);
                    } else {
                        updateGradeStepPlacementMap.put("p_effective_date",
                                                        new java.sql.Date(new java.util.Date().getTime()));
                    }
                    updateGradeStepPlacementMap.put("p_placement_id",
                                                    placementId);
                    updateGradeStepPlacementMap.put("p_datetrack_mode",
                                                    gradeModeSet);
                    updateGradeStepPlacementMap.put("p_object_version_number",
                                                    (BigDecimal)selectedGradeRow.getObjectVersionNumber());
                    updateGradeStepPlacementMap.put("p_step_id",
                                                    (BigDecimal)selectedGradeRow.getStepId());
                    updateGradeStepPlacementMap.put("p_reason",
                                                    (String)selectedGradeRow.getReasonCode());
                    Map updateGradeStepPlacementReturnMap =
                        new UpdateGradeStepPlacementAPICall().callUpdateGradeStepPlacementAPI(updateGradeStepPlacementMap);
                    Set keys = updateGradeStepPlacementReturnMap.keySet();
                    Iterator itr = keys.iterator();
                    String key = null;
                    Object value = null;
                    while (itr.hasNext()) {
                        key = (String)itr.next();
                        value = updateGradeStepPlacementReturnMap.get(key);
                        if ("p_error".equalsIgnoreCase(key) &&
                            (updateGradeStepPlacementReturnMap.get("p_error") !=
                             null)) {
                            System.out.println("+++bean++++Error: " +
                                               updateGradeStepPlacementReturnMap.get("p_error"));
                            errorMessage =
                                    (String)updateGradeStepPlacementReturnMap.get("p_error");
                            UIUtil.showPopUp(errorPopup);
                        } else {
                            errorMessage =
                                    "Grade Step Placement updated Successfully!";
                            UIUtil.showPopUp(errorPopup);
                        }
                    }
                }
            }
        }
        gradeModeSet = null;
        return null;
    }

    public void setUpdateAndCorrectionGradePopupBind(RichPopup updateAndCorrectionGradePopupBind) {
        this.updateAndCorrectionGradePopupBind = updateAndCorrectionGradePopupBind;
    }

    public RichPopup getUpdateAndCorrectionGradePopupBind() {
        return updateAndCorrectionGradePopupBind;
    }

    public String gradeUpdateModeSet() {
        // Add event code here...
        gradeModeSet = "UPDATE";
        return saveGradeStepPlacementDetails();
    }

    public String gradeCorrectionModeSet() {
        // Add event code here...
        gradeModeSet = "CORRECTION";
        return saveGradeStepPlacementDetails();
    }

    public void setChangeReasonFlag(boolean changeReasonFlag) {
        this.changeReasonFlag = changeReasonFlag;
    }

    public boolean isChangeReasonFlag() {
        return changeReasonFlag;
    }

    public void setChangeReasonPopUpBind(RichPopup changeReasonPopUpBind) {
        this.changeReasonPopUpBind = changeReasonPopUpBind;
    }

    public RichPopup getChangeReasonPopUpBind() {
        return changeReasonPopUpBind;
    }
    public void changeReasonDialogListener(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome() == DialogEvent.Outcome.yes) {
                changeReasonFlag=false;
                changeReasonPopUpBind.cancel();
            }
        if (dialogEvent.getOutcome() == DialogEvent.Outcome.no) {
            changeReasonFlag=false;
            assignmentRecordSave();
        }
    }

    public void setUpdateModeChangerPopupBind(RichPopup updateModeChangerPopupBind) {
        this.updateModeChangerPopupBind = updateModeChangerPopupBind;
    }

    public RichPopup getUpdateModeChangerPopupBind() {
        return updateModeChangerPopupBind;
    }

    public void setSupervisorIsValid(boolean supervisorIsValid) {
        this.supervisorIsValid = supervisorIsValid;
    }

    public boolean isSupervisorIsValid() {
        return supervisorIsValid;
    }
    
    private void toMakePrimayAssignment(Number assignmentId, int personId) {
        
        System.out.println("...toMakePrimayAssignment....assignmentId.."+assignmentId);
        System.out.println("...toMakePrimayAssignment....personId.."+personId);

        java.sql.Date effectiveDatesql = null;
        Connection connection = null;
        PreparedStatement preparedstatement = null;
       BigDecimal objVersionNumber =null;
         Map nonPrimaryAssignmentDetails = new HashMap();
                
                 if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("p_effective_date") !=      null) {
                     Date effectiveDate = (java.util.Date)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("p_effective_date");
                     effectiveDatesql = new java.sql.Date(effectiveDate.getTime());
                     nonPrimaryAssignmentDetails.put("p_effective_date",   effectiveDatesql  );
                     
                 } else {    
                         effectiveDatesql =new java.sql.Date(new java.util.Date().getTime());
                     nonPrimaryAssignmentDetails.put("p_effective_date",  new java.sql.Date(new java.util.Date().getTime()));   
                     }
                
            nonPrimaryAssignmentDetails.put("p_assignment_id", assignmentId);
            nonPrimaryAssignmentDetails.put("p_person_id", personId);
        
                                          //========logic to get p_object_version_number===============Start===================
                
                                              try {
                                              connection = ADFUtil.getConnection();
                                                  System.out.println("......getConnection .in try.......");
                                               String sqlQueryForVersionNumber ="select OBJECT_VERSION_NUMBER as ObjectVersionNumber from per_all_assignments_f where assignment_id=? and (? between effective_start_date and effective_end_date)";
                                                  // "select (sum(value)) as SumValueWTE from PER_ASSIGNMENT_BUDGET_VALUES_F where assignment_id IN (select assignment_id from per_all_assignments_f where person_id=? and ? between effective_start_date and effective_end_Date)";
                                                preparedstatement = connection.prepareStatement(sqlQueryForVersionNumber);
                                                 preparedstatement.setInt(1, assignmentId.intValue());
                                                   preparedstatement.setDate(2, effectiveDatesql);
                                                  ResultSet resultSet = preparedstatement.executeQuery();  
                                                     while (resultSet.next()) {
                                                                System.out.println("......in while............");
                                                                objVersionNumber = (BigDecimal)resultSet.getObject("ObjectVersionNumber");
                                                                System.out.println("..in while......objVersionNumber..." + objVersionNumber.intValue());
                                                                break; //as we are expecting only one row
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        } finally {
                                                            if (preparedstatement != null) {
                                                                try {
                                                                    preparedstatement.close();
                                                                } catch (SQLException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }
                                              //========logic to get p_object_version_number================end==================

        nonPrimaryAssignmentDetails.put("p_object_version_number", objVersionNumber.intValue());
       // new UpdateAssignmentAPICall().callMakePrimaryAssignmentAPI(updatePrimayAssignmentMap);
        
              Map primaryAssignmentReturnMap =   new SetPrimaryAssignmentAPICall().callMakePrimaryAssignmentAPI(nonPrimaryAssignmentDetails);
                              Set keys = primaryAssignmentReturnMap.keySet();
                              Iterator itr = keys.iterator();
                              String key = null;
                              Object value = null;
                              while (itr.hasNext()) {
                                  key = (String)itr.next();
                                  value = primaryAssignmentReturnMap.get(key);
                                                      
                                  if ("p_error".equalsIgnoreCase(key) &&     (primaryAssignmentReturnMap.get("p_error") != null)) {
                                      System.out.println("+++bean++++Error: " + primaryAssignmentReturnMap.get("p_error"));
                                      errorMessage =  (String)primaryAssignmentReturnMap.get("p_error");
                                      UIUtil.showPopUp(errorPopup);
                                  }
                                      else {
                                          primaryFlagIsDisable = true;
                                          errorMessage = "Current Assignment has been updated to Primary Assignment. And all other assignment details also updated Successfully...!";
                                           UIUtil.showPopUp(errorPopup);
                                      }
        
        
                        }
               
             }

    public void setCoreFormNavigationLinkIsDisable(boolean coreFormNavigationLinkIsDisable) {
        this.coreFormNavigationLinkIsDisable = coreFormNavigationLinkIsDisable;
    }

    public boolean isCoreFormNavigationLinkIsDisable() {
        return coreFormNavigationLinkIsDisable;
    }    
 /*   public void payrollValueChangeListener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        System.out.println("-----Inside payroll change listener----");
        oracle.jbo.domain.Date assignmentEffEndDate = null;
        if (futureChangesPresentMessage) {
                UIUtil.showPopUp(payrollChangePopUpBind);
        }    
    }*/
    
 public void payrollValueChangeListener(ValueChangeEvent valueChangeEvent) {
     // Add event code here...
     System.out.println("-----Inside payroll change listener----");        
     if (futureChangesPresentMessage) {
             payrollValueChangeListenerCalled = true;
             UIUtil.showPopUp(updateAndCorreectionPopupBind);
     }    
 }

    public void setPayrollChangePopUpBind(RichPopup payrollChangePopUpBind) {
        this.payrollChangePopUpBind = payrollChangePopUpBind;
    }

    public RichPopup getPayrollChangePopUpBind() {
        return payrollChangePopUpBind;
    }

    public String saveBasicAssignmentDetails() {
        // Add event code here...
        DCBindingContainer bindings =(DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItrBindings =bindings.findIteratorBinding("AssignmentSearchVO1Iterator");
        ViewObject voTabledata = dcItrBindings.getViewObject();
        AssignmentSearchVORowImpl selectedRow = (AssignmentSearchVORowImpl)voTabledata.getCurrentRow();  
        Number gradeId = (Number)selectedRow.getGradeId();
        Number payrollId  = (Number)selectedRow.getPayrollId();
        Number positionId = (Number)selectedRow.getPositionId();
        Number assignmentId = (Number)selectedRow.getAssignmentId();
        java.sql.Date effectiveDatesql = null;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("p_effective_date") !=null) {
            Date effectiveDate =(java.util.Date)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("p_effective_date");
            effectiveDatesql = new java.sql.Date(effectiveDate.getTime());
           } else {
            effectiveDatesql =new java.sql.Date(new java.util.Date().getTime());
        }
   if(assignmentId !=null){
        Map validateContractTypeMap = new HashMap();
        validateContractTypeMap.put("p_effective_date", effectiveDatesql);
        validateContractTypeMap.put("p_assignment_id", assignmentId);
        validateContractTypeMap.put("p_payroll_id", payrollId);
        validateContractTypeMap.put("p_grade_id", gradeId);
        validateContractTypeMap.put("p_position_id", positionId);
       
        Map validateContractTypeReturnMap = new ValidateContractTypeAPICall().validateContractType(validateContractTypeMap);
        
        Set keys = validateContractTypeReturnMap.keySet();
        Iterator itr = keys.iterator();
        String key = null;
        Object value = null;
        while (itr.hasNext()) {
            key = (String)itr.next();
            value = validateContractTypeReturnMap.get(key);
//            if ("p_error".equalsIgnoreCase(key) &&(validateContractTypeReturnMap.get("p_error") != null)) {
//                System.out.println("+++bean++++Error: " +validateContractTypeReturnMap.get("p_error"));
//                errorMessage =(String)validateContractTypeReturnMap.get("p_error");
//                UIUtil.showPopUp(validateContractTypePopup);
//                return null;
//            } 
            if ("p_error".equalsIgnoreCase(key) &&(validateContractTypeReturnMap.get("p_error") != null) && !contractTypePopUpShown) {
                System.out.println("+++bean++++Error: " +validateContractTypeReturnMap.get("p_error"));
                errorMessage =(String)validateContractTypeReturnMap.get("p_error");
                contractTypePopUpShown = true;
                UIUtil.showPopUp(validateContractTypePopup);
                return null;
            }

          }     
     }
          
        return assignmentRecordSave();
    }

    public String setOldPayrollValue() {
        // Add event code here...
        DCBindingContainer bindings =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItrBindings =
            bindings.findIteratorBinding("AssignmentSearchVO1Iterator");
        ViewObject voTabledata = dcItrBindings.getViewObject();
        AssignmentSearchVORowImpl selectedRow = (AssignmentSearchVORowImpl)voTabledata.getCurrentRow();
        selectedRow.setPayrollId(basePayrollId);
        selectedRow.setPayrollName(basePayrollName);
        return null;
    }

    public String closePayrollPopUp() {
        // Add event code here...
        payrollChangePopUpBind.cancel();
        return null;
    }

    public void setValidateContractTypePopup(RichPopup validateContractTypePopup) {
        this.validateContractTypePopup = validateContractTypePopup;
    }

    public RichPopup getValidateContractTypePopup() {
        return validateContractTypePopup;
    }

    public String resetValidateContractTypePArams() {
        // Add event code here...
        DCBindingContainer bindings =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItrBindings =
            bindings.findIteratorBinding("AssignmentSearchVO1Iterator");
        ViewObject voTabledata = dcItrBindings.getViewObject();
        AssignmentSearchVORowImpl selectedRow = (AssignmentSearchVORowImpl)voTabledata.getCurrentRow();
        selectedRow.setGradeId(baseGradeId);
        selectedRow.setPayrollId(basePayrollId);
        selectedRow.setPositionId(basePositionId);
        selectedRow.setGradeName(baseGradeName);
        selectedRow.setPayrollName(basePayrollName);
        selectedRow.setPositionName(basePositionName);
        return null;
    }

    public void setReplaceAndInsertPayrollPopupBind(RichPopup replaceAndInsertPayrollPopupBind) {
        this.replaceAndInsertPayrollPopupBind = replaceAndInsertPayrollPopupBind;
    }

    public RichPopup getReplaceAndInsertPayrollPopupBind() {
        return replaceAndInsertPayrollPopupBind;
    }
    
    public String insertPayrollModeSetting() {
        UIUtil.showPopUp(payrollChangePopUpBind);
        return null;
    }
    
    public String closeValidationContractTypePopUp() {
        // Add event code here...
        validateContractTypePopup.cancel();
        return null;
    }
    
    public String refreshAssignmentAction() {
            System.out.println("...refreshAssignmentAction.....");
            FacesContext fctx = FacesContext.getCurrentInstance();
            ExternalContext ectx = fctx.getExternalContext();
            Map<String, Object> scopeMap = ectx.getSessionMap();
     try{
            DCBindingContainer bindings =(DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
            DCIteratorBinding dcItrBindings = bindings.findIteratorBinding("AssignmentSearchVO1Iterator");
            ViewObject voTabledata = dcItrBindings.getViewObject();
            System.out.println("... get.from session..assignmentId.."+scopeMap.get("assignmentId"));
            
            voTabledata.setWhereClause("assignment_id = "+ scopeMap.get("assignmentId"));
                voTabledata.executeQuery();
           
            Row selectedRow =null;
            if(voTabledata.getEstimatedRowCount()>0){
            selectedRow = voTabledata.getCurrentRow();
                System.out.println("..voTabledata..count.."+voTabledata.getEstimatedRowCount());
                System.out.println("... selectedRow...assignment_id..."+selectedRow.getAttribute("AssignmentId"));
            }
        } catch(Exception e){
            e.getMessage();
            }
    
            updationModeSet = null;
           return "refresh";
        }


//    public void setCreateUpdateEffDateBind(RichInputDate createUpdateEffDateBind) {
//        this.createUpdateEffDateBind = createUpdateEffDateBind;
//        
//        if (this.createUpdateEffDateBind.getValue() == null || this.createUpdateEffDateBind.getValue().toString().equals("")) {
//            try {
//                if (DateUtil.getEffectiveDate() != null) {
//                    this.createUpdateEffDateBind.setValue(DateUtil.getEffectiveDate());
//                }else{
//                    this.createUpdateEffDateBind.setValue(new Date());
//                    DateUtil.setEffectiveDate(new java.sql.Date(new Date().getTime()));
//                }
//            } catch (ParseException e) {
//                System.out.println("+++++++++++++++++ Exception while reading effective date from session");
//            }
//
//        }
//        
//    }
//
//    public RichInputDate getCreateUpdateEffDateBind() {
//        return createUpdateEffDateBind;
//    }
}
