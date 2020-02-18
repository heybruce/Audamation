package pageobjects.processstep;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.PageObject;
import utils.log.Loggable;

import java.time.Duration;
import java.util.*;
import java.util.NoSuchElementException;

public class DamageCapturingPO extends PageObject {
    //Loading Circle
    public static final By PAGE_LOADING_CIRCLE = By.cssSelector(".loader-big .content .loader-circle");
    public static final By INSIDE_LOADING_CIRCLE = By.cssSelector("#content .loader-big .content .loader-circle");
    public static final By QAPTER_CONTAINER = By.id("qapter-container");
    public static final By QAPTER_IFRAME = By.id("iframe_root.task.damageCapture.inlineWebPad");
    public static final String CSS_REPAIR_COST = "#repair-cost-value .number";
    public static final By ID_QAPTER_GRAPHICS_NOT_AVAILABLE = By.id("graphics-not-available");
    public static final By ID_POPUP_MODEL_OPTION = By.id("popup-modal");
    public static final By ID_MODEL_OPTION_VIEW = By.id("dynamic-display-model-options-view");
    public static final By ID_ADD_POSITION_MAIN_SECTION = By.id("insert-damage-position-main-section");
    public static final By ID_REPAIR_PANEL_MAIN_SECTION = By.id("repair-panel-main-section");
    public static final By ID_SEARCH_REPAIR_PANEL_VIEW = By.id("search-repair-panel-view");
    public static final By ID_OEM_PART_NUMBER = By.id("oem-part-number");
    public static final String GET_ATTRIBUTE_CLASS = "class";
    public static final String GET_ATTRIBUTE_VALUE = "value";
    private static final String CLASS_VALUE_VISIBLE= "visible";
    public static final By ID_PICTOGRAM_SHEET = By.id("pictograms-grid");
    private static final By ID_PICTOGRAM_IMAGE = By.id("Capa_1");
    public static final By MODEL_OPTION_LOADING = By.cssSelector("#dynamic-display-model-options-view .loader-big .content .loader-circle");
    public static final String ID_REPAIR_METHOD_REPAIR = "repair-method-selector-I";
    public static final String ID_REPAIR_METHOD_SURFACE_PAINTING = "repair-method-selector-L";
    public static final String ID_REPAIR_METHOD_REPAIR_PAINTING = "repair-method-selector-LI";
    public static final String REPAIR_METHOD_SELECTED = "repair-method-selected";
    public static final String SECTION_HIDDEN = "section-hidden";
    private static final String ID_MUTATIONS_INPUT_FIELD_WU = "mutations-input-text-field-wu";
    private static final String ID_MUTATIONS_INPUT_FIELD_GM = "mutations-input-text-field-GM";
    public static final By KR_REPAIR_ERROR_DIALOG = By.cssSelector(".korean-repair-error-dialog-content");
    public static final By KR_REPAIR_ERROR_DIALOG_BLANKET = By.id("modal-blanket");
    public static final String ID_BTN_CONTINUE_IN_REPAIR_PARAMETERS = "repair-parameters-continue-btn";
    private static final String CLASS_CHECKLIST_EDITABLE_FIELD = "checklist-editable-field";
    public static final By CSS_MORE_PARTS_ACTIONS_PICTOGRAM = By.cssSelector(".more-parts-actions-pictogram");
    private static final By ID_PHOTO_VIEW = By.id("photo-viewer");
    private static final By ID_MODEL_OPTIONS_SECTIONS = By.id("model-options-sections");
    private static final String ID_CUSTOM_MO = "custom-x";
    public static final String ID_CUSTOM_MO_DESC = "model-option-custom-x-description";
    private static final String ID_MO_DESC = "model-option-x-description";
    private static final String ID_MODEL_OPTION_EDIT_ICON = "model-option-edit-icon";
    private static final String ID_MODEL_OPTION_DELETE_ICON = "model-option-delete-icon";
    public static final String CSS_MODEL_OPTION_GROUP = ".model-option-description";
    private static final String ID_MODEL_OPTION_EDIT_CONTINUE_BTN = "edit-model-option-continue-button";
    private static final String ID_SELECT_MODEL_OPTION_CODE_PREFIX = "s-m";
    private static final String ID_MODEL_OPTION_EDIT_CANCEL_BTN = "edit-model-option-cancel-button";
    private static final String ID_MODEL_OPTION_CONFLICT_DIALOG = "un-confirm";
    private static final String ID_MODEL_OPTION_CODE_PREFIX = "m";
    private static final String ID_MODEL_OPTION_ALL_SELECTED_OPTION = "all-selected-options";
    public static final By ID_CALC_PREVIEW_PRINT = By.id("navigation-print");
    private static final String CSS_CHECKLIST_MORE_INFO_CONTENT = "#checklist-position-more-info-x .checklist-position-more-info-content";
    private static final String ID_CHECKLIST_FILTER_CONTAINER = "modalContainer";
    private static final String XPATH_FAST_CAPTURING_MULTIPART_ITEM = "//*[@id=\"multi-part-modal-wrapper\"]/ul/li[.//span[text()=\"name\"]]";
    public static final By ID_VEHICLE_MAIN_VIEW = By.id("sheet_0");
    // iOS
    public static final String XPATH_CAMERA_ALLOW_BTN = "//XCUIElementTypeButton[@name=\"허용\"]";

    @FindBy(id = "root.task.damageCapture.webpadImage")
    private WebElement qapterIcon;

    //iframe
    @FindBy(id="iframe_root.task.damageCapture.inlineWebPad")
    private WebElement iframeQapter;
    @FindBy(tagName = "iframe")
    private  WebElement iframe;

    //toolbar
    @FindBy(id="navigation-vehicle")
    private WebElement navigationVehicle;
    @FindBy(id="navigation-photo")
    private WebElement navigationPhoto;
    @FindBy(id="navigation-search")
    private WebElement navigationSearch;
    @FindBy(id="navigation-adjustment")
    private WebElement navigationAdjustment;
    @FindBy(id="navigation-checklist")
    private WebElement navigationChecklist;
    @FindBy(id="navigation-settings")
    private WebElement navigationSettings;
    @FindBy(id="navigation-info")
    private WebElement navigationInfo;
    @FindBy(id="navigation-receipt")
    private WebElement navigationCalcPreview;
    @FindBy(css=CSS_REPAIR_COST)
    private WebElement repairCostNumber;
    @FindBy(css="#repair-cost-value .currency-symbol")
    private WebElement repairCostCurrency;
    @FindBy(id="new_position")
    private WebElement addNewPosition;
    @FindBy(id="subhead-alert-area")
    private WebElement subheadAlert;
    @FindBy(css="#subhead-alert-area p")
    private WebElement warningMessage;

    //breadcrumb
    @FindBy(id="breadcrumb-title")
    private WebElement breadcrumbTitle;

    //Add new position - standard part
    @FindBy(id="insert-damage-position-type-standard")
    private WebElement standardPostionTab;
    @FindBy(id="standard-position-guide-number-input-section")
    private WebElement inputSPGuideNumber;
    @FindBy(id="standard-position-select-repair-method")
    private WebElement spSelectRepairMethod;
    @FindBy(id="standard-position-repair-method-E")
    private WebElement spReplaceWithOEMPart;
    @FindBy(id="insert-standard-position-action")
    private WebElement spAddPosition;
    @FindBy(id="insert-plus-standard-position-action")
    private WebElement spKeepAdding;
    @FindBy(id="add-another-position-button")
    private WebElement spAddAnotherPosition;
    //Add new position - non-standard part
    @FindBy(id="insert-damage-position-type-non-standard-value")
    private WebElement nonStandardPostionTab;
    @FindBy(id="nsp-repair-method-S")
    private WebElement nspSupplementary;
    @FindBy(id="nsp-repair-method-E")
    private WebElement nspreplaceWithOEMPart;
    @FindBy(id="nsp-repair-method-N")
    private WebElement nspRemovalRefitting;
    @FindBy(id="nsp-repair-method-I")
    private WebElement nspRepair;
    @FindBy(id="nsp-repair-method-V")
    private WebElement nspOpticalAlignment;
    @FindBy(id="nsp-repair-method-H")
    private WebElement nspHollowCavitySealing;
    @FindBy(id="nsp-repair-method-LE")
    private WebElement nspNewPartPainting;
    @FindBy(id="nsp-repair-method-LI")
    private WebElement nspRepairPainting;
    @FindBy(id="nsp-repair-method-L")
    private WebElement nspSurfacePainting;
    @FindBy(id="nsp-mandatory-description-item")
    private WebElement nspDescription;
    @FindBy(id="nsp-mandatory-spare-part-item")
    private WebElement nspSparePart;
    @FindBy(id="nsp-optional-labour-item")
    private WebElement nspWorkUnits;
    @FindBy(id="nsp-optional-amount-item")
    private WebElement nspAmount;
    @FindBy(id="nsp-add-nsp-footer-item")
    private WebElement nspAddPosition;
    @FindBy(id="nsp-adding-nsp-footer-item")
    private WebElement nspKeepAdding;
    @FindBy(id="nsp-add-as-predefined-item-label")
    private WebElement checkboxAddAsPNSP;
    //Add new position - predefined NSP
    @FindBy(id="nsp-open-predefined-nsp-item")
    private WebElement selectPredefinedNsp;

    //Predefined NSP List
    private static final By ID_PNSP_LIST_HEADER = By.id("nsp-modal-header");
    public static final By ID_NO_PNSP_CONTENT = By.id("no-predefined-nsp-results-content");
    @FindBy(id="nsp-table-body")
    private WebElement pnspListBody;
    @FindBy(css=".nsp-table-body-entry")
    private List<WebElement> pnspListItems;
    @FindBy(css=".nsp-table-body-entry-group-checkbox-label")
    private List<WebElement> pnspListAddIcon;
    @FindBy(css=".nsp-table-body-entry-description-text")
    private List<WebElement> pnspListPartDescription;
    @FindBy(css=".nsp-table-body-entry-text")
    private List<WebElement> pnspListRepairMethod;
    @FindBy(css=".nsp-table-body-entry-description-guide-number")
    private List<WebElement> pnspListGuideNumber;
    @FindBy(css=".nsp-table-body-entry-spare-text")
    private List<WebElement> pnspListPartNumber;
    @FindBy(css=".nsp-table-body-entry-info-work-units-value")
    private List<WebElement> pnspListWorkUnits;
    @FindBy(css=".nsp-table-body-entry-info-amount-value-text")
    private List<WebElement> pnspListPartPrice;
    @FindBy(id="nsp-table-footer-button-done")
    private WebElement btnPNSPListDone;
    @FindBy(id="nsp-table-footer-button-cancel")
    private WebElement btnPNSPListCancel;
    @FindBy(id="no-predefined-nsp-results-btn")
    private WebElement btnBackForNoPNSPResult;
    @FindBy(id="nsp-modal-header-close-icon")
    private WebElement btnClosePNSPList;

    //Select side
    private static final By ID_SELECT_SIDE = By.id("select-side");
    @FindBy(id="left-side")
    private WebElement leftSide;
    @FindBy(id="right-side")
    private WebElement rightSide;

    //Repair Panel
    @FindBy(id="repair-panel-main-section")
    private WebElement repairPanelMainSection;
    @FindBy(id="insert-damage-position-main-section")
    private WebElement insertDamageRepairPanelMainSection;
    @FindBy(id="repair-method-selector-E")
    private WebElement btnReplaceWithOEMPart;
    @FindBy(id="repair-method-selector-N")
    private WebElement btnRemovalAndRefitting;
    @FindBy(id=ID_REPAIR_METHOD_REPAIR)
    private WebElement btnRepair;
    @FindBy(id="repair-method-selector-H")
    private WebElement btnHollowCavitySealing;
    @FindBy(id="repair-operations-group-header-paint")
    private WebElement btnPaint;
    @FindBy(id=ID_REPAIR_METHOD_SURFACE_PAINTING)
    private WebElement btnSurfacePainting;
    @FindBy(id="repair-method-selector-LE")
    private WebElement btnNewPartPainting;
    @FindBy(id="repair-method-selector-LI")
    private WebElement btnRepairPainting;
    @FindBy(id="rp-part-description")
    private WebElement rptextPartDescription;
    @FindBy(css="#part-price-value .price-amount")
    private WebElement rptextPartPriceAmount;
    @FindBy(id="oem-part-number")
    private WebElement rptextPartNumber;
    @FindBy(id="rp-guidenumber-value")
    private WebElement rptextGuideNumber;
    @FindBy(id="close-repair-panel-icon-holder")
    private WebElement closeRepairPanel;
    @FindBy(id="insert-damage-position-close-icon")
    private WebElement closeInsertPositionRepairPanel;
    private static final String ID_UPLOAD_REPAIR_PANEL = "upload-repair-panel";
    @FindBy(id=ID_UPLOAD_REPAIR_PANEL)
    private WebElement uploadPhotos;
    @FindBy(css="#part-photo .img-content")
    private List<WebElement> partPhotos;
    @FindBy(css = ".multiple-parts-item-operation-count")
    private List<WebElement> multiplePartsOperationCount;
    public static final By ID_CENTER_SIDE_COUNT = By.id("center-side-count");

    //mutations
    @FindBy(id="repair-panel-mutations-section")
    private WebElement repairPanelMutationsSection;
    @FindBy(id="rm-mutations-L")
    private WebElement surfacePaintingMutations;
    @FindBy(id="rm-mutations-I")
    private WebElement repairMutations;
    @FindBy(id="rm-mutations-E")
    private WebElement replaceMutations;
    @FindBy(id="repair-parameters-parts-composition-button")
    private WebElement partsCompositionBtn;
    @FindBy(id="rp-parts-composition-wrapper")
    private WebElement partsCompositionView;
    @FindBy(css="#rp-parts-composition-main td.left")
    private WebElement mainPartName;
    @FindBy(id="rp-parts-composition-extra")
    private WebElement extraPartContainer;
    @FindBy(css="#rp-parts-composition-extra tbody tr")
    private List<WebElement> extraParts;
    @FindBy(css="#rp-parts-composition-work tbody tr")
    private List<WebElement> workCompositionPart;
    //Repair parameters
    @FindBy(id="repair-panel-repair-parameters-section")
    private WebElement repairParametersSection;
    @FindBy(css=".korean-repair-severity-item .korean-repair-section-item-selector")
    private List<WebElement> krSeverityTypeSelector;
    @FindBy(id=ID_BTN_CONTINUE_IN_REPAIR_PARAMETERS)
    private WebElement btnContinueInRepairParameters;
    @FindBy(id="korean-repair-severity-section")
    private WebElement repairSeveritySection;
    @FindBy(id="korean-repair-damage-section")
    private WebElement damageTypeSection;
    @FindBy(css="#korean-repair-damage-section .korean-repair-section-item-selector")
    private List<WebElement> krDamageItemSelector;
    @FindBy(id="part-price-input-section-field")
    private WebElement oemPartPriceInRepairParameter;
    @FindBy(id="korean-total-working-units")
    private WebElement totalWorkingUnit;
    @FindBy(css=".btn-confirm")
    private WebElement btnConfirm;

    //Mutation
    @FindBy(id=ID_MUTATIONS_INPUT_FIELD_WU)
    private  WebElement textFieldMutations;
    @FindBy(className = "proceed-btn")
    private WebElement btnContinue;

    //New pictograms
    @FindBy(id="pictogram-view-label")
    private WebElement btnMoreView;
    @FindBy(css=".more-views-item")
    private List<WebElement> moreViewItems;
    @FindBy(css=".more-parts-actions-pictogram")
    private List<WebElement> morePartsPictogram;

    //Checklist
    @FindBy(id=ID_CHECKLIST_TABLE)
    private WebElement checklistTable;
    private static final String ID_CHECKLIST_TABLE = "checklist-table-body-section";
    @FindBy(css=".checklist-body-checkbox")
    private List<WebElement> checklistCheckbox;
    @FindBy(css=".part-description")
    private List<WebElement> checklistPartDescription;
    @FindBy(css=".guide-number")
    private List<WebElement> checklistGuideNumber;
    @FindBy(css=".checklist-body-part-number")
    private List<WebElement> checklistPartNumber;
    @FindBy(css=".price")
    private List<WebElement> checklistPrice;
    @FindBy(css=".checklist-body-price")
    private List<WebElement> checklistPriceEditable;
    @FindBy(css="#checklist-table-body-section .repair-method")
    private List<WebElement> checklistRepairMethod;
    @FindBy(css=".labour")
    private List<WebElement> checklistLabour;
    @FindBy(css=".checklist-trash-item")
    private List<WebElement> checklistDeleteBtn;
    @FindBy(id="check-list-subhead-section-positions")
    private WebElement checklistPosition;
    @FindBy(id="check-list-subhead-section-modeloptions")
    private WebElement checklistModelOptions;
    @FindBy(id="check-list-results-modeloptions")
    private WebElement checklistModelOptionTable;
    @FindBy(id="check-list-new-position")
    private WebElement btnChecklistNewPosition;
    @FindBy(id="check-list-delete-positions")
    private WebElement btnDeletePositionInChecklist;
    @FindBy(css=".clmo-row")
    private List<WebElement> checklistModelOptionRow;
    @FindBy(css=".checklist-info-item")
    private List<WebElement> checklistMoreInfoBtn;
    @FindBy(id="check-list-view-filter")
    private WebElement viewFilter;
    @FindBy(css="#checklist-filter-content-positions .checklist-filter-content-filter-option-value")
    private List<WebElement> positionsFilter;
    @FindBy(css="#checklist-filter-content-repair-methods .checklist-filter-content-filter-option-value")
    private List<WebElement> repairMethodsFilter;
    @FindBy(css="#checklist-filter-content-zones .checklist-filter-content-filter-option-value")
    private List<WebElement> zonesFilter;
    @FindBy(css="#checklist-filter-content-comments-and-photos .checklist-filter-content-filter-option-value")
    private List<WebElement> commentsAndPhotosFilter;
    @FindBy(id="checklist-filter-apply")
    private WebElement applyFilterBtn;
    @FindBy(id="checklist-filter-content-repair-method-E")
    private WebElement replaceWithOemFilter;

    //Calculation Preview
    @FindBy(id="calc-preview")
    private WebElement calcPreview;
    public static final String ID_CALCU_PREVIEW_TEXT = "calc-preview-details-preformatted-text";
    @FindBy(id=ID_CALCU_PREVIEW_TEXT)
    private WebElement calcPreviewText;
    public static final String ID_CALCU_PREVIEW_PDF = "calc-preview-details-pdf";
    @FindBy(id=ID_CALCU_PREVIEW_PDF)
    private WebElement calcPreviewPdf;


    //Zone
    @FindBy(id="dynamic-display-model-options")
    private WebElement btnModelOptionInZone;
    @FindBy(id="multiple_parts")
    private WebElement btnFastCapturing;
    @FindBy(id="zone-tree-navigation-arrow")
    private WebElement zoneListDropdown;
    @FindBy(id="navigation-vehichle-tree-navigation-arrow")
    private WebElement zoneListDropdownCollapse;
    @FindBy(id="graphics-not-available-link")
    private WebElement graphicsNotAvailableMessage;
    @FindBy(id="repair-panel")
    private WebElement zoneContainer;

    //Fast Capturing
    @FindBy(id="fast-capturing-repair-method-desription")
    private WebElement selectRepairMethod;
    @FindBy(xpath="//span[@data-value='left']")
    private WebElement fastCapturingLeftSide;
    @FindBy(xpath="//span[@data-value='right']")
    private WebElement fastCapturingRightSide;
    @FindBy(xpath="//span[@data-value='center']")
    private WebElement fastCapturingNoSide;
    @FindBy(id="fast-capturing-done")
    private WebElement fastCapturingDone;
    @FindBy(id="multi-part-modal-wrapper")
    private WebElement fastCapturingMultipartList;

    //Select repair method
    @FindBy(id="fast-capturing-repair-method-E")
    private WebElement fcReplaceWithOemPart;
    @FindBy(id="fast-capturing-repair-method-E-All")
    private WebElement fcReplaceWithAllPart;
    @FindBy(id="fast-capturing-repair-method-N")
    private WebElement fcRemovalAndRefitting;
    @FindBy(id="fast-capturing-repair-method-N-All")
    private WebElement fcRefitAllParts;
    @FindBy(id="fast-capturing-repair-method-I")
    private WebElement fcRepair;
    @FindBy(id="fast-capturing-repair-method-L")
    private WebElement fcSurfacePainting;
    @FindBy(id="fast-capturing-repair-method-L-All")
    private WebElement fcPaintAllParts;
    @FindBy(id="fast-capturing-repair-method-LE")
    private WebElement fcNewPartPainting;
    @FindBy(id="fast-capturing-repair-method-LI")
    private WebElement fcRepairPainting;
    @FindBy(id="fast-capturing-repair-method-LI1")
    private WebElement fcRepairPaintingOver50;

    //multipart panel
    @FindBy(id="multiple-parts-section")
    private WebElement multipartsSection;

    //model option
    @FindBy(css=CSS_MODEL_OPTION_GROUP)
    private List<WebElement> modelOptionGroup;
    @FindBy(id="continue-button")
    private WebElement btnMoContinue;
    @FindBy(id="model-option-description-mW3")
    private WebElement w3Door;
    @FindBy(id="model-option-description-mW5")
    private WebElement w5Door;
    @FindBy(id="dynamic-display-zone-relevant-options-close-icon")
    private WebElement btnCloseModelOptionView;
    @FindBy(id="add-custom-model-option")
    private WebElement btnAddCustomMO;
    @FindBy(id="add-custom-model-options-input-field")
    private WebElement customMODescription;
    @FindBy(id="add-custom-model-options-continue-button")
    private WebElement customMOContinueBtn;
    @FindBy(id="model-options-section-all-selected")
    private WebElement allSelectedModelOptions;
    @FindBy(id="edited-model-option-input-field")
    private WebElement editedModelOptionDescriptionInput;
    @FindBy(id=ID_MODEL_OPTION_EDIT_CONTINUE_BTN)
    private WebElement editedModelOptionContinueBtn;
    @FindBy(className="model-options-section-description")
    private List<WebElement> modelOptionZones;
    @FindBy(css=".btn.btn-confirm")
    private WebElement continueBtn;
    @FindBy(id=ID_MODEL_OPTION_EDIT_CANCEL_BTN)
    private WebElement editedModelOptionCancelBtn;
    @FindBy(id="mo-count-all-selected")
    private WebElement allSelectedModelOptionsNumber;

    //Search
    private static final String XPATH_SEARCH_TABLE = "//*[@id=\"search-results-table\"]/tbody/tr[" ;
    @FindBy(id="inputSearch")
    private WebElement inputSearch;
    @FindBy(id="search-button")
    private WebElement searchBtn;
    private static final String XPATH_PART_SEARCH_DESCRIPTION = "td/div[@id=\"part-search-description\"]";
    private static final String XPATH_SHEET_SEARCH_DESCRIPTION = "td/div[@id=\"sheet-search-description\"]";
    private static final String XPATH_SEARCH_MODEL_OPTIONS = "td[contains(@class, \"modelOptionsMultiline\")]";
    @FindBy(css=".part-preview")
    private WebElement isolatedPartPreview;
    @FindBy(css=".search-result-part")
    private List<WebElement> searchResultPart;
    @FindBy(id="search-results")
    private WebElement searchResults;

    //Settings
    @FindBy(id="user-settings-vehicle-value-input")
    private WebElement inputVehicleValue;
    @FindBy(id = "3d-view-checkbox-slide")
    private WebElement threeDViewIndicator;
    @FindBy(id = "box")
    private WebElement threeDViewSwitch;

    //keypad for mobile device
    @FindBy(id="keypad-wrapper")
    private WebElement keypadWrapper;
    @FindBy(id="keypad-numbers")
    private WebElement keypadNumbers;
    @FindBy(xpath = "//*[@id=\"keypad-footer\"]/button")
    private WebElement keypadConfirm;
    @FindBy(css = ".keypad-clear-all-icon")
    private WebElement keypadClearAll;

    //Navigation
    public static final By ARROW_NAVIGATION = By.id("arrow-navigation");
    public static final By ARROW_RIGHT = By.id("an-arrow-right");
    public static final By ARROW_LEFT = By.id("an-arrow-left");
    public static final By ARROW_DOWN = By.id("an-arrow-down");
    public static final By ARROW_UP = By.id("an-arrow-up");

    //Navigation tree
    @FindBy(id = "navigation-breadcrumbs")
    private WebElement navigationBreadcrumbs;
    @FindBy(id = "tree-navigation-zones-container")
    private WebElement navigationTreeZoneList;
    @FindBy(id = "tree-navigation-zone-icon-32")
    private WebElement navigationZoneIconFrontOuter;
    private static final String CLASS_NAVIGATION_ZONE_PART = ".tree-navigation-zone-part";
    @FindBy(css=CLASS_NAVIGATION_ZONE_PART)
    private List<WebElement> navigationPartsList;
    @FindBy(css = ".tree-navigation-zone-part-icon  ")
    private List<WebElement> navigationPartsIconList;

    //Photos
    private static final String CLASS_PHOTO_HOLDER = "photo-holder";
    private static final String CSS_VIEW_SORT_TITLE = ".sort-title.sort-zone";
    @FindBy(id = "upload")
    private WebElement uploadPhotosCenterBtn;
    @FindBy(id = "view-all")
    private WebElement viewAll;
    @FindBy(id = "view-zone")
    private WebElement viewbyZone;
    @FindBy(id = "view-part")
    private WebElement viewbyPart;
    @FindBy(css = CSS_VIEW_SORT_TITLE)
    private List<WebElement> viewSortTitle;
    @FindBy(className = "sort-container")
    private List<WebElement> viewSortContainer;

    public DamageCapturingPO() {
        super();
    }

    public DamageCapturingPO(WebDriver webDriver) {
        super(webDriver);
    }

    @Loggable
    public void clickQapterIcon() {
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.visibilityOf(qapterIcon));
        new WebDriverWait(webDriver, 30).until(ExpectedConditions.attributeToBe(qapterIcon, "role", "presentation"));
        new WebDriverWait(webDriver, 30).until(ExpectedConditions.elementToBeClickable(qapterIcon));
        this.click(qapterIcon);
        switchToQapterIframe();
    }

    @Loggable
    public void waitForQapterLoading(){ waitForElementInvisible(INSIDE_LOADING_CIRCLE); }

    @Loggable
    public void waitForModelOptionLoading(){ waitForElementInvisible(MODEL_OPTION_LOADING); }

    @Loggable
    public void switchToQapterIframe(){
        new WebDriverWait(webDriver, 30).until(ExpectedConditions.visibilityOfElementLocated(QAPTER_CONTAINER));
        // workaround for the issue that chromedriver doesn't wait for iframe to fully load
        waitForQapterIframeReady();
        webDriver.switchTo().frame(webDriver.findElement(QAPTER_IFRAME));
        new WebDriverWait(webDriver, 15).until(ExpectedConditions.visibilityOf(navigationVehicle));
        waitForQapterLoading();
    }

    @Loggable
    private void waitForQapterIframeReady() {
        WebElement iframe = webDriver.findElement(QAPTER_IFRAME);
        Wait<WebDriver> wait = new FluentWait<>(webDriver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(3));
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return arguments[0].contentDocument.readyState", iframe).equals("complete"));
    }

    @Loggable
    public void navigationVehicle(){
        this.click(navigationVehicle);
        waitForElementInvisible(INSIDE_LOADING_CIRCLE);
    }

    @Loggable
    public boolean isTopOfVehicleView(){
        return navigationVehicle.getAttribute("class").contains("visible");
    }

    @Loggable
    public void navigationPhoto(){
        this.click(navigationPhoto);
        waitForElementInvisible(INSIDE_LOADING_CIRCLE);
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.visibilityOfElementLocated(ID_PHOTO_VIEW));
    }

    @Loggable
    public void navigationSearch(){
        this.click(navigationSearch);
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.visibilityOf(inputSearch));
    }

    @Loggable
    public void navigationModelOptions(){
        this.click(navigationAdjustment);
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.visibilityOfElementLocated(ID_MODEL_OPTIONS_SECTIONS));
    }

    @Loggable
    public void navigationChecklist() {
        this.click(navigationChecklist);
        waitForQapterLoading();
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated(By.id(ID_CHECKLIST_TABLE)));
    }

    @Loggable
    public void navigationChecklist(long timeOutInSeconds) {
        this.click(navigationChecklist);
        waitForQapterLoading();
        new WebDriverWait(webDriver, timeOutInSeconds).until(ExpectedConditions.presenceOfElementLocated(By.id(ID_CHECKLIST_TABLE)));
    }

    @Loggable
    public void navigationSettings(){
        this.click(navigationSettings);
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.visibilityOf(inputVehicleValue));
    }

    @Loggable
    public void navigationInfo(){ this.click(navigationInfo); }

    @Loggable
    public void navigationCalcPreview(){
        this.click(navigationCalcPreview);
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.visibilityOf(calcPreview));
        waitForElementInvisible(INSIDE_LOADING_CIRCLE);
    }

    @Loggable
    public void clickZone(String webElementId) {
        new WebDriverWait(webDriver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.id(webElementId)));
        new Actions(webDriver).moveToElement(webDriver.findElement(By.id(webElementId))).click().perform();
        waitForElementInvisible(INSIDE_LOADING_CIRCLE);
    }
    @Loggable
    public void clickPosition(String webElementId)  {
        new WebDriverWait(webDriver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.id(webElementId)));
        new Actions(webDriver).moveToElement(webDriver.findElement(By.id(webElementId))).click().perform();
    }

    @Loggable
    public boolean isPartMarked(String webElementId){
        WebElement partElement = webDriver.findElement(By.id(webElementId));
        if(partElement.getAttribute(GET_ATTRIBUTE_CLASS).contains("preview"))
            return true;
        else
            return false;

    }

    @Loggable
    public void clickRPReplaceWithOEMPart(){ this.click(btnReplaceWithOEMPart); }

    @Loggable
    public void clickRPRemovalAndRefitting(){ this.click(btnRemovalAndRefitting); }

    @Loggable
    public void clickRPRepair(){
        waitForElementRefreshedAndVisible(rptextPartPriceAmount);
        this.click(btnRepair);
    }

    @Loggable
    public void clickRPPaint() { this.click(btnPaint); }

    @Loggable
    public void clickRPSurfPainting(){ this.click(btnSurfacePainting); }

    @Loggable
    public void clickRPNewPartPainting(){ this.click(btnNewPartPainting); }

    @Loggable
    public void clickRPRepairPainting(){ this.click(btnRepairPainting); }

    @Loggable
    public void clickRPHollowCavitySealing() {
        this.click(btnHollowCavitySealing);
    }

    @Loggable
    public void inputMutations(String mutations){ this.sendKeys(textFieldMutations, mutations); }

    @Loggable
    public void clickMutationsTextField(){ this.click(textFieldMutations); }

    @Loggable
    public String getMutationsWU(){ return this.getAttributeValue(By.id(ID_MUTATIONS_INPUT_FIELD_WU), GET_ATTRIBUTE_VALUE); }

    @Loggable
    public String getMutationsGM(){ return this.getAttributeValue(By.id(ID_MUTATIONS_INPUT_FIELD_GM), GET_ATTRIBUTE_VALUE); }

    @Loggable
    public void clickContinue(){ this.click(btnContinue); }

    @Loggable
    public void clickCloseRepairPanel(){ this.click(closeRepairPanel); }

    @Loggable
    public void clickCloseInsertPositionRepairPanel(){ this.click(closeInsertPositionRepairPanel); }

    @Loggable
    public void uploadPhotosOnRepairPanel(String filepath) {
        this.waitForElementPresent(By.id(ID_UPLOAD_REPAIR_PANEL));
        this.uploadPhotos.sendKeys(filepath);
        waitForElementInvisible(INSIDE_LOADING_CIRCLE);
    }

    @Loggable
    public List<String> getAllPhotoIDOnRepairPanel() {
        List<String> photoIDs = new ArrayList<>();
        for (WebElement ele : partPhotos) {
            // In order to make photo ID format consistent with other methods, remove extra "photo-" string from ID
            photoIDs.add(ele.getAttribute("id").replace("photo-", ""));
        }
        return photoIDs;
    }

    @Loggable
    public String getIncreaseRepairCost(String originalCost){
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.invisibilityOfElementWithText(By.cssSelector(CSS_REPAIR_COST), originalCost));
        repairCostNumber = waitForElementRefreshedAndVisible(repairCostNumber);
        return this.getText(repairCostNumber);
    }

    @Loggable
    public void clickAddNewPosition() {
        this.click(addNewPosition);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.visibilityOfElementLocated(ID_ADD_POSITION_MAIN_SECTION));
    }

    @Loggable
    public void clickNonStandardTab(){
        new WebDriverWait(webDriver, 10)
                .until(ExpectedConditions.visibilityOf(nonStandardPostionTab));
        this.click(nonStandardPostionTab);
        waitForQapterLoading();
    }

    @Loggable
    public void clickNSPSupplementary(){ this.click(nspSupplementary); }

    @Loggable
    public void clickNSPReplacewithOEM(){ this.click(nspreplaceWithOEMPart); }

    @Loggable
    public void clickNSPRemoveAndRefitting(){ this.click(nspRemovalRefitting); }

    @Loggable
    public void clickNSPRepair(){ this.click(nspRepair); }

    @Loggable
    public void clickNSPOpticalAligment(){ this.click(nspOpticalAlignment); }

    @Loggable
    public void clickNSPHollowCavitySealing(){ this.click(nspHollowCavitySealing); }

    @Loggable
    public void clickNSPNewPartPainting(){ this.click(nspNewPartPainting); }

    @Loggable
    public void clickNSPRepairPainting(){ this.click(nspRepairPainting); }

    @Loggable
    public void clickNSPSurfacePainting(){ this.click(nspSurfacePainting); }

    @Loggable
    public void inputNSPDescription(String description){ this.sendKeys(nspDescription, description); }

    @Loggable
    public void inputNSPSparePart(String sparePart){ this.sendKeys(nspSparePart, sparePart); }

    @Loggable
    public void inputNSPWorkUnits(String workUnits){ this.sendKeys(nspWorkUnits,workUnits); }

    @Loggable
    public void clickNSPWorkUnits(){ this.click(nspWorkUnits); }

    @Loggable
    public void inputNSPAmount(String amount){ this.sendKeys(nspAmount,amount); }

    @Loggable
    public void clickNSPAmount(){ this.click(nspAmount); }

    @Loggable
    public void clickNSPAddPosition(){
        this.click(nspAddPosition);
        waitForQapterLoading();
    }

    @Loggable
    public void clickNSPKeepAdding(){ this.click(nspKeepAdding); }

    @Loggable
    public void clickAddAsPNSPCheckbox(){ this.click(checkboxAddAsPNSP); }

    @Loggable
    public void clickSelectPredefinedNSP(){ this.click(selectPredefinedNsp); }

    @Loggable
    public int getSizeOfPNSPList(){
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.visibilityOf(pnspListBody));
        return pnspListItems.size(); }

    @Loggable
    public void clickPNSPAddIcon(int index){
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.visibilityOf(pnspListBody));
        this.click(pnspListAddIcon.get(index));
    }

    @Loggable
    public void clickPNSPListDoneButton(){
        this.click(btnPNSPListDone);
        this.waitForElementInvisible(ID_PNSP_LIST_HEADER);
    }

    @Loggable
    public void clickPNSPListCancelButton(){ this.click(btnPNSPListCancel);}

    @Loggable
    public void clickClosePNSPList(){ this.click(btnClosePNSPList); }

    @Loggable
    public void clickBackForNoPNSPButton(){ this. click(btnBackForNoPNSPResult); }

    @Loggable
    public String getPNSPListPartDescription(int index){ return pnspListPartDescription.get(index).getText(); }

    @Loggable
    public String getPNSPListGuideNumber(int index){ return pnspListGuideNumber.get(index).getText(); }

    @Loggable
    public String getPNSPListPartNumber(int index){ return pnspListPartNumber.get(index).getText(); }

    @Loggable
    public String getPNSPListPartPrice(int index){ return pnspListPartPrice.get(index).getText(); }

    @Loggable
    public String getPNSPListRepairMethod(int index){ return pnspListRepairMethod.get(index).getText(); }

    @Loggable
    public String getPNSPListWorkUnits(int index){ return pnspListWorkUnits.get(index).getText(); }

    @Loggable
    public void clickMoreViewToOpenPictogram(){
        this.click(btnMoreView);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.attributeContains(By.id("pictogram-view-label"),GET_ATTRIBUTE_CLASS, "active"));
    }

    @Loggable
    public void clickMoreViewToClosePictogram(){
        this.click(btnMoreView);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.attributeContains(By.id("pictogram-view-label"), GET_ATTRIBUTE_CLASS, ""));
    }

    @Loggable
    public int getChecklistNumber(){
        return checklistTable.findElements(By.xpath("//div[contains(@class, 'row-wrapper')]")).size();
    }

    @Loggable
    public void clickChecklistCheckbox(int row){ this.click(checklistCheckbox.get(row)); }

    @Loggable
    public String getChecklistPartDescription(int row){ return checklistPartDescription.get(row).getText(); }

    @Loggable
    public String getChecklistGuideNumber(int row){ return checklistGuideNumber.get(row).getText(); }

    @Loggable
    public String getChecklistPartNumber(int row){
        row = row-1;
        if(checklistPartNumber.get(row).getAttribute(GET_ATTRIBUTE_CLASS).contains(CLASS_CHECKLIST_EDITABLE_FIELD))
            return checklistPartNumber.get(0).getAttribute(GET_ATTRIBUTE_VALUE);
        return checklistPartNumber.get(row).getText();
    }

    @Loggable
    public String getChecklistPrice(int row){
        if(!checklistPriceEditable.isEmpty())
            return checklistPriceEditable.get(0).getAttribute(GET_ATTRIBUTE_VALUE);
        return checklistPrice.get(row).getText();
    }

    @Loggable
    public String getChecklistRepairMethod(int row){ return checklistRepairMethod.get(row - 1).getText(); }

    @Loggable
    public String getChecklistLabour(int row){
        return checklistLabour.get(row).findElement(By.xpath("div/input")).getAttribute(GET_ATTRIBUTE_VALUE);
    }

    @Loggable
    public void deleteChecklistItem(int row){ this.click(checklistDeleteBtn.get(row)); }

    @Loggable
    public void clickChecklistPositionTab(){ this.click(checklistPosition); }

    @Loggable
    public void clickChecklistModelOptions(){
        this.click(checklistModelOptions);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.visibilityOf(checklistModelOptionTable));
    }

    @Loggable
    public void openMoreInfoOfChecklistItem(int row){ this.click(checklistMoreInfoBtn.get(row - 1)); }

    @Loggable
    public boolean isMoreInfoOfChecklistItemOpened(int row){
        return !webDriver.findElements(By.cssSelector(CSS_CHECKLIST_MORE_INFO_CONTENT.replace("x", String.valueOf(row - 1)))).isEmpty();
}

    @Loggable
    public void openFilterOnChecklist() {
        this.click(viewFilter);
        new WebDriverWait(webDriver, 2).until(ExpectedConditions.visibilityOfElementLocated(
                By.id(ID_CHECKLIST_FILTER_CONTAINER)));
    }

    @Loggable
    private List<String> getFilterItems(List<WebElement> filterElement) {
        List<String> filterItems = new ArrayList<>();
        for (WebElement ele : filterElement) {
            filterItems.add(ele.getText());
        }
        return filterItems;
    }

    @Loggable
    public List<String> getPositionsFilterItems() {
        return getFilterItems(positionsFilter);
    }

    @Loggable
    public List<String> getRepairMethodsFilterItems() {
        return getFilterItems(repairMethodsFilter);
    }

    @Loggable
    public List<String> getZonesFilterItems() {
        return getFilterItems(zonesFilter);
    }

    @Loggable
    public List<String> getCommentsAndPhotosFilterItems() {
        return getFilterItems(commentsAndPhotosFilter);
    }

    @Loggable
    public void clickRPReplaceWithOEMFilter() {
        replaceWithOemFilter.click();
        new WebDriverWait(webDriver, 1).until(ExpectedConditions.attributeContains(replaceWithOemFilter,
                GET_ATTRIBUTE_CLASS, "active"));
    }

    @Loggable
    public void applyFilter() {
        this.click(applyFilterBtn);
        new WebDriverWait(webDriver, 2).until(ExpectedConditions.invisibilityOfElementLocated(
                By.id(ID_CHECKLIST_FILTER_CONTAINER)));
        waitForElementInvisible(INSIDE_LOADING_CIRCLE);
    }

    @Loggable
    public boolean isRepairMethodFiltered(String method) {
        for (WebElement ele : checklistRepairMethod) {
            if (!ele.getText().contains(method)) {
                return false;
            }
        }
        return true;
    }

    @Loggable
    public String getRPPartDescription(){ return this.getText(rptextPartDescription); }

    @Loggable
    public String getRPPartPriceAmount(){ return this.getText(rptextPartPriceAmount); }

    @Loggable
    public String getRPPartNumber(){ return this.getText(rptextPartNumber); }

    @Loggable
    public String getRPGuideNumber(){ return this.getText(rptextGuideNumber); }

    @Loggable
    public String getCalcPreviewContent() { return this.getText(calcPreviewText); }

    @Loggable
    public boolean isSelectSidePopUp(){
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.visibilityOf(repairPanelMainSection));
        return !webDriver.findElements(ID_SELECT_SIDE).isEmpty();
    }

    @Loggable
    public void clickLeftSide(){
        this.click(leftSide);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.attributeContains(leftSide,GET_ATTRIBUTE_CLASS, "side-selector-active"));
    }

    @Loggable
    public void clickRightSide(){ this.click(rightSide); }

    @Loggable
    public void clickModelOptionInZone(){ this.click(btnModelOptionInZone); }

    @Loggable
    public void clickFastCapturing(){ this.click(btnFastCapturing); }

    @Loggable
    public void clickSelectRepairMethod(){ this.click(selectRepairMethod); }

    @Loggable
    public void clickFastCaptureLeftSide(){ this.click(fastCapturingLeftSide); }

    @Loggable
    public void clickFastCaptureRightSide(){ this.click(fastCapturingRightSide); }

    @Loggable
    public void clickFastCaptureNoSide(){ this.click(fastCapturingNoSide); }

    @Loggable
    public void clickFcReplaceWithOem(){ this.click(fcReplaceWithOemPart); }

    @Loggable
    public void clickFcReplaceAllParts(){ this.click(fcReplaceWithAllPart); }

    @Loggable
    public void clickFcRemoveAndRefitting(){ this.click(fcRemovalAndRefitting); }

    @Loggable
    public void clickFcRefitAllParts(){ this.click(fcRefitAllParts); }

    @Loggable
    public void clickFcRepair(){ this.click(fcRepair); }

    @Loggable
    public void clickFcSurfacePainting(){ this.click(fcSurfacePainting); }

    @Loggable
    public void clickFcPaintAllParts(){ this.click(fcPaintAllParts); }

    @Loggable
    public void clickFcNewPartPainting(){ this.click(fcNewPartPainting); }

    @Loggable
    public void clickFcRepairPainting(){ this.click(fcRepairPainting); }

    @Loggable
    public void clickFcRepairPaintingOver50(){ this.click(fcRepairPaintingOver50); }

    @Loggable
    public void clickDoneInFastCapturing(){ this.click(fastCapturingDone); }

    @Loggable
    public void clickFastCapturingMultipartsPosition(String... multiPartPositions) {
        new WebDriverWait(webDriver, 2).until(ExpectedConditions.visibilityOf(fastCapturingMultipartList));
        for (String multiPartPosition : multiPartPositions) {
            webDriver.findElement(By.xpath(XPATH_FAST_CAPTURING_MULTIPART_ITEM.replace("name", multiPartPosition))).click();
        }
    }

    @Loggable
    public void clickMultipartsPosition(String damagePosition){
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.visibilityOf(multipartsSection));
        this.click(webDriver.findElement(By.xpath("//span[@class='multiple-part-selector-text' and contains(text(), '"+ damagePosition +"')]")));
    }

    @Loggable
    public WebElement getMultiplePartsOperationCount(String guideNo){
        for(WebElement partCount: multiplePartsOperationCount) {
            String partGuideNumber = partCount.findElement(By.xpath("./..")).getAttribute("data-part-guide-number");
            if(partGuideNumber.equals(guideNo)) {
                return partCount;
            }
        }
        return null;
    }

    @Loggable
    public boolean isPartWithRepairIcon(String guideNo) {
        return webDriver.findElement(By.cssSelector(".part-selector[data-guide-number=\"" + guideNo + "\"] .true")).isDisplayed();
    }

    @Loggable
    public void inputSearch(String query){
        this.sendKeys(inputSearch, query);
        new WebDriverWait(webDriver, 3).until(
                ExpectedConditions.attributeContains(By.id("search-results-number"), GET_ATTRIBUTE_CLASS, CLASS_VALUE_VISIBLE));
        new WebDriverWait(webDriver, 3).until(
                ExpectedConditions.attributeContains(searchResults, GET_ATTRIBUTE_CLASS, CLASS_VALUE_VISIBLE));
    }

    @Loggable
    public void inputSearchOem(String query){ this.sendKeys(inputSearch, query);}

    @Loggable
    public void clickSearchBtn(){
        //Wait for search button to be enabled
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.attributeToBe(searchBtn, GET_ATTRIBUTE_CLASS, ""));
        //Send space key event to trigger click since there is an element covered the search button
        searchBtn.sendKeys(Keys.SPACE);
        new WebDriverWait(webDriver, 3).until(
                ExpectedConditions.attributeContains(By.id("search-results-number"), GET_ATTRIBUTE_CLASS, CLASS_VALUE_VISIBLE));
        new WebDriverWait(webDriver, 3).until(
                ExpectedConditions.attributeContains(searchResults, GET_ATTRIBUTE_CLASS, CLASS_VALUE_VISIBLE));
    }

    @Loggable
    public String getSearchResultPartDescription(){ return this.getText(visibleSearchResultPart().findElement(By.xpath(XPATH_PART_SEARCH_DESCRIPTION))); }

    @Loggable
    public String getSearchResultSheetDescription(){ return this.getText(visibleSearchResultPart().findElement(By.xpath(XPATH_SHEET_SEARCH_DESCRIPTION))); }

    @Loggable
    public String getSearchResultGuideNo(){
        return this.getText(webDriver.findElement(By.xpath(XPATH_SEARCH_TABLE+getVisibleSearchResultIndex()+"]/td[3]")));}

    @Loggable
    public String getSearchResultModelOptions(){ return this.getText(visibleSearchResultPart().findElement(By.xpath(XPATH_SEARCH_MODEL_OPTIONS)));}

    @Loggable
    public String getOemPartSearchResultOemCode(){ return this.getText(webDriver.findElement(By.xpath(XPATH_SEARCH_TABLE+getVisibleSearchResultIndex()+"]/td[4]")));}

    @Loggable
    public String getOemPartSearchResultModelAndSubModel(){ return this.getText(webDriver.findElement(By.xpath(XPATH_SEARCH_TABLE+getVisibleSearchResultIndex()+"]/td[6]")));}

    @Loggable
    public void clickSearchResultPartDescription(){
        this.click(visibleSearchResultPart().findElement(By.xpath(XPATH_PART_SEARCH_DESCRIPTION)));
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.visibilityOfElementLocated(ID_SEARCH_REPAIR_PANEL_VIEW));
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.not(ExpectedConditions.textToBe(ID_OEM_PART_NUMBER, "")));
    }

    @Loggable
    public void clickSearchResult(){
        this.click(visibleSearchResultPart().findElement(By.xpath("td[1]")));
    }

    private WebElement visibleSearchResultPart(){
        WebElement visibleResult = searchResultPart.get(0);
        for(WebElement result: searchResultPart) {
            if (!result.getAttribute(GET_ATTRIBUTE_CLASS).contains(SECTION_HIDDEN))
                return result;
        }
        return visibleResult;
    }

    private int getVisibleSearchResultIndex(){
        int index = 1;
        for(WebElement result: searchResultPart) {
            if (result.getAttribute(GET_ATTRIBUTE_CLASS).contains(SECTION_HIDDEN))
                index++;
            else
                break;
        }
        return index;
    }

    @Loggable
    public boolean isIsolatedPartPreviewDisplayed() { return isolatedPartPreview.isDisplayed();}

    @Loggable
    public String selectSubModel(int row){
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.presenceOfElementLocated(ID_POPUP_MODEL_OPTION));
        this.click(modelOptionGroup.get(row));
        return modelOptionGroup.get(row).getText();
    }

    @Loggable
    public void clickModelOptionContinue(){ this.click(btnMoContinue); }

    @Loggable
    public void clickW5Door() {
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.visibilityOfElementLocated(ID_MODEL_OPTION_VIEW));
        this.click(w5Door);
    }

    @Loggable
    public void clickCloseModelOptionView(){ this.click(btnCloseModelOptionView); }

    @Loggable
    public void clickChecklistNewPosition(){
        this.click(btnChecklistNewPosition);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.visibilityOfElementLocated(ID_ADD_POSITION_MAIN_SECTION));
    }

    @Loggable
    public void inputSPGuideNumber(String guideNo){ this.sendKeys(inputSPGuideNumber, guideNo); }

    @Loggable
    public void clickSPSelectRepairMethod(){
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.not(
                ExpectedConditions.attributeContains(spSelectRepairMethod, GET_ATTRIBUTE_CLASS, "disabled")));
        this.click(spSelectRepairMethod);
    }

    @Loggable
    public void clickSPReplaceWithOem(){ this.click(spReplaceWithOEMPart); }

    @Loggable
    public void clickSPKeepAdding(){
        this.click(spKeepAdding);
        waitForElementInvisible(INSIDE_LOADING_CIRCLE);
    }

    @Loggable
    public void clickSPAddAnotherPosition(){
        this.click(spAddAnotherPosition);
        waitForElementInvisible(INSIDE_LOADING_CIRCLE);
    }

    @Loggable
    public void clickSPAddPosition(){ this.click(spAddPosition); }

    @Loggable
    public void clickChecklistItem(int row){
        this.click(checklistGuideNumber.get(row));
        new WebDriverWait(webDriver, 3).until(
                ExpectedConditions.or(
                        ExpectedConditions.visibilityOf(repairPanelMainSection),
                        ExpectedConditions.visibilityOf(insertDamageRepairPanelMainSection)
                ));
    }

    @Loggable
    public void clickDeletePositionInChecklist(){
        this.click(btnDeletePositionInChecklist);
        waitForQapterLoading();
    }

    @Loggable
    public boolean isPartSelected(String webElementId){
        boolean isSelected = false;
        WebElement part = webDriver.findElement(By.id(webElementId));
        if (!part.findElements(By.cssSelector(".selected")).isEmpty())
            isSelected = true;
        return isSelected;
    }

    @Loggable
    public void clickOutOfRepairPanel(){ this.click(webDriver.findElement(By.cssSelector(".subhead-zone-area-options-navigation"))); }

    @Loggable
    public void inputVehicleValue(String value){ this.sendKeys(inputVehicleValue, value); }

    @Loggable
    public boolean isRepairCostAlert(){
        boolean isAlert = false;
        if (subheadAlert.getAttribute(GET_ATTRIBUTE_CLASS).contains("state-high"))
            isAlert = true;
        return isAlert;
    }

    @Loggable
    public String getWarningMessage(){ return this.getText(warningMessage); }

    @Loggable
    public int getPictogramNumber(){ return moreViewItems.size(); }

    @Loggable
    public int getMorePartsPictogramNumber(){
        int pictogramDisplayNum = 0;
        for(WebElement morePartsEle: morePartsPictogram) {
            WebElement parentNode = morePartsEle.findElement(By.xpath(".."));
            if (!parentNode.getAttribute("style").contains("display: none"))
                pictogramDisplayNum++;
        }
        return pictogramDisplayNum;
    }

    @Loggable
    public List<WebElement> getMorePartsPictogram(){ return morePartsPictogram; }

    @Loggable
    public int getAllSelectedModelOptionNumber(){ return Integer.valueOf(this.getText(allSelectedModelOptionsNumber));}

    @Loggable
    public int getModelOptionNumberInChecklist(){ return checklistModelOptionRow.size(); }

    @Loggable
    public Map<String, String> getModelOptionsInChecklist(String code) {
        Map<String, String> modelOption = new HashMap<>();
        for (WebElement element : checklistModelOptionRow) {
            //Get model option code and type
            String moCode = element.findElement(By.cssSelector("td:nth-child(1) span")).getText();
            String moType = element.findElement(By.cssSelector("td:nth-child(2) span")).getText();
            String moDescription = element.findElement(By.cssSelector("td:nth-child(3) span")).getText();
            if (moCode.equals(code)) {
                modelOption.put("moCode", moCode);
                modelOption.put("moType", moType);
                modelOption.put("moDescription", moDescription);
            }
        }
        return modelOption;
    }

    @Loggable
    public Map<String, String> getCustomModelOptionsInChecklist(String description) {
        Map<String, String> modelOption = new HashMap<>();
        for (WebElement element : checklistModelOptionRow) {
            //Get model option code and type
            String moType = element.findElement(By.cssSelector("td:nth-child(2) span")).getText();
            String moDescription = element.findElement(By.cssSelector("td:nth-child(3) span")).getText();
            if (moDescription.startsWith(description.toUpperCase())) {
                modelOption.put("moType", moType);
                modelOption.put("moDescription", moDescription);
            }
        }
        return modelOption;
    }

    @Loggable
    public boolean isPictogramWithImage(){
        boolean isWithImage = true;
        for(WebElement pictogram: moreViewItems) {
            WebElement pictogramDiv = pictogram.findElement(By.xpath("div"));
            if(!pictogramDiv.getAttribute(GET_ATTRIBUTE_CLASS).contains("disable") &&
                    pictogramDiv.findElements(ID_PICTOGRAM_IMAGE).isEmpty()){
                isWithImage = false;
            }
        }
        return isWithImage;
    }

    @Loggable
    public void selectKRSeverityType(int type){
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.visibilityOf(repairSeveritySection));
        this.click(krSeverityTypeSelector.get(type));
    }

    @Loggable
    public void selectKRDamageItem(int type){
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.visibilityOf(damageTypeSection));
        this.click(krDamageItemSelector.get(type));
    }

    @Loggable
    public int getDamageTypeItemNumber(){ return krDamageItemSelector.size(); }

    @Loggable
    public String getDamageTypeName(int item){ return krDamageItemSelector.get(item).getText(); }

    @Loggable
    public void inputOemPartPrice(double price){ this.sendKeys(oemPartPriceInRepairParameter, String.valueOf(price)); }

    @Loggable
    public String getOemPartPrice(){ return oemPartPriceInRepairParameter.getAttribute(GET_ATTRIBUTE_VALUE); }

    @Loggable
    public String getTotalWorkingUnits(){ return totalWorkingUnit.getText(); }

    @Loggable
    public void clickContinueInRepairParameters(){ this.click(btnContinueInRepairParameters); }

    @Loggable
    public void clickSurfacePaintingMutations(){
        this.click(surfacePaintingMutations);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.not(
                ExpectedConditions.attributeContains(repairPanelMutationsSection, GET_ATTRIBUTE_CLASS, SECTION_HIDDEN)));
    }

    @Loggable
    public void clickRepairMutationsOfKoreanRepair(){
        this.click(repairMutations);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.visibilityOf(repairParametersSection));
    }

    @Loggable
    public void clickReplaceMutations(){
        this.click(replaceMutations);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.visibilityOf(repairParametersSection));
    }

    @Loggable
    public void clickPartsComposition(){
        this.click(partsCompositionBtn);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.visibilityOf(partsCompositionView));
    }

    @Loggable
    public String getMainPartName() {
        return mainPartName.getText();
    }

    @Loggable
    public Boolean isExtraPartDisplayed() {
        try {
            return extraPartContainer.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Loggable
    public Boolean isAllExtraPartSelected() {
        for (WebElement part : extraParts) {
            //Extra part list may be long, need to scroll into view first
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", part);
            if(!part.getAttribute(GET_ATTRIBUTE_CLASS).contains("selected")) {
                return false;
            }
        }
        return true;
    }

    @Loggable
    public Map<String, List> getWorkCompositionData() {
        //Store Work Composition part name, WU and Price into a map.
        Map<String, List> workCompositionData = new HashMap<>();
        for (WebElement part : workCompositionPart) {
            //To retrieve data of item that may need to scroll into view first
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", part);
            workCompositionData.put(getWorkCompositionName(part), getWorkCompositionWuAndPrice(part));
        }
        return workCompositionData;
    }

    @Loggable
    private String getWorkCompositionName(WebElement workCompositionEle) {
        return workCompositionEle.findElement(By.className("left")).getText();
    }

    @Loggable
    private List getWorkCompositionWuAndPrice(WebElement workCompositionEle) {
        List<String> wuAndPriceList = new ArrayList<>();
        for (WebElement ele : workCompositionEle.findElements(By.className("right"))) {
            wuAndPriceList.add(ele.getText());
        }
        return wuAndPriceList;
    }

    @Loggable
    public String getRepairErrorMessage(){ return webDriver.findElement(KR_REPAIR_ERROR_DIALOG).getText(); }

    @Loggable
    public void clickBtnCloseInRepairErrorDialog(){
        this.click(btnConfirm);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.invisibilityOfElementLocated(KR_REPAIR_ERROR_DIALOG_BLANKET));
    }

    private WebElement getKeypadNumberElement(String number){
        return keypadNumbers.findElement(By.xpath("//button[text()=\""+number+"\"]"));
    }

    @Loggable
    public void clickKeypadNumber(String number){
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.visibilityOf(keypadWrapper));
        getKeypadNumberElement(number).click();
    }

    @Loggable
    public void clickKeypadConfirm(){
        this.click(keypadConfirm);
    }

    @Loggable
    public void clickKeypadClearAll(){
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.visibilityOf(keypadWrapper));
        this.click(keypadClearAll);
    }

    @Loggable
    public void click3dViewSwitch() {
        this.click(threeDViewSwitch);
    }

    @Loggable
    public WebElement getThreeDViewIndicator() {
        return threeDViewIndicator;
    }

    @Loggable
    public void clickZoneListDropdown() {
        this.click(zoneListDropdown);
    }

    @Loggable
    public void clickZoneListDropdownCollapse() {
        this.click(zoneListDropdownCollapse);
    }

    @Loggable
    public void waitForRepairPanelDisplay() {
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.visibilityOfElementLocated(ID_REPAIR_PANEL_MAIN_SECTION));
    }

    @Loggable
    public boolean isRPRepairSelected() {
        waitForElementRefreshedAndVisible(btnRepair);
        return btnRepair.getAttribute(GET_ATTRIBUTE_CLASS).contains(REPAIR_METHOD_SELECTED);
    }

    @Loggable
    public boolean isRPSurfacePaintingSelected() {
        waitForElementRefreshedAndVisible(btnSurfacePainting);
        return btnSurfacePainting.getAttribute(GET_ATTRIBUTE_CLASS).contains(REPAIR_METHOD_SELECTED);
    }

    @Loggable
    public boolean isRPReapirPaintingSelected() {
        waitForElementRefreshedAndVisible(btnRepairPainting);
        return btnRepairPainting.getAttribute(GET_ATTRIBUTE_CLASS).contains(REPAIR_METHOD_SELECTED);
    }

    @Loggable
    public void clickZoneNavigationRightArrow(){
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.visibilityOfElementLocated(ARROW_NAVIGATION));
        this.click(webDriver.findElement(ARROW_RIGHT));
        waitForQapterLoading();
    }

    @Loggable
    public void clickZoneNavigationLeftArrow(){
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.visibilityOfElementLocated(ARROW_NAVIGATION));
        this.click(webDriver.findElement(ARROW_LEFT));
        waitForQapterLoading();
    }

    @Loggable
    public void clickZoneNavigationDownArrow(){
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.visibilityOfElementLocated(ARROW_NAVIGATION));
        this.click(webDriver.findElement(ARROW_DOWN));
        waitForQapterLoading();
    }

    @Loggable
    public void clickZoneNavigationUpArrow() {
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.visibilityOfElementLocated(ARROW_NAVIGATION));
        this.click(webDriver.findElement(ARROW_UP));
        waitForQapterLoading();
    }

    public String getGraphicsNotAvailableMessage() {
        return this.getText(graphicsNotAvailableMessage);
    }

    @Loggable
    public void clickNavigationBreadcrumbs(){
        this.click(navigationBreadcrumbs);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.visibilityOf(navigationTreeZoneList));
    }

    @Loggable
    public void clickNavigationZoneIconFrontOuter(boolean toExpandList){
        this.click(navigationZoneIconFrontOuter);
        if(toExpandList)
            new WebDriverWait(webDriver, 3).until(
                    ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector(CLASS_NAVIGATION_ZONE_PART), 0));
        else
            new WebDriverWait(webDriver, 3).until(
                    ExpectedConditions.numberOfElementsToBe(By.cssSelector(CLASS_NAVIGATION_ZONE_PART), 0));
    }

    @Loggable
    public void clickNavigationPartsIcon(String guideNumber){
        for(WebElement navigationPartsIcon: navigationPartsIconList) {
            if (navigationPartsIcon.getAttribute("data-guide-number").equals(guideNumber)
                    && !navigationPartsIcon.findElement(By.xpath("../../..")).getAttribute(GET_ATTRIBUTE_CLASS).contains(SECTION_HIDDEN)) {
                this.click(navigationPartsIcon);
                break;
            }
        }
        waitForRepairPanelDisplay();
    }

    @Loggable
    public void clickNavigationParts(String guideNumber){
        for(WebElement navigationParts: navigationPartsList) {
            if(navigationParts.getAttribute("data-guide-number").equals(guideNumber)
                    && !navigationParts.findElement(By.xpath("..")).getAttribute(GET_ATTRIBUTE_CLASS).contains(SECTION_HIDDEN)) {
                this.click(navigationParts);
                break;
            }
        }
        waitForQapterLoading();
    }

    public void clickAddCustomModelOptionButton() { this.click(btnAddCustomMO); }

    @Loggable
    public void inputCustomModelOptionDescription(String text) { this.sendKeys(customMODescription, text);}

    @Loggable
    public void clickCustomModelOptionContinue() { this.click(customMOContinueBtn); }

    @Loggable
    public void clickAllSelectedModelOptions() {
        this.click(allSelectedModelOptions);
        new WebDriverWait(webDriver, 2).until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_MODEL_OPTION_ALL_SELECTED_OPTION)));
    }

    @Loggable
    public String getCustomModelOptionDescription(int index) {
        String locator = ID_CUSTOM_MO_DESC.replace("x", String.valueOf(index));
        return this.getText(webDriver.findElement(By.id(locator)));
    }

    @Loggable
    public String getModelOptionDescription(String modelOptionCode) {
        String locator = ID_MO_DESC.replace("x", ID_MODEL_OPTION_CODE_PREFIX + modelOptionCode);
        return this.getText(webDriver.findElement(By.id(locator)));
    }

    @Loggable
    public void clickEditCustomModelOption(int index) {
        String locator = "div#" + ID_CUSTOM_MO.replace("x", String.valueOf(index)) + " #" + ID_MODEL_OPTION_EDIT_ICON;
        this.click(webDriver.findElement(By.cssSelector(locator)));
    }

    @Loggable
    public void clickDeleteCustomModelOption(int index) {
        String locator = "div#" + ID_CUSTOM_MO.replace("x", String.valueOf(index)) + " #" + ID_MODEL_OPTION_DELETE_ICON;
        this.click(webDriver.findElement(By.cssSelector(locator)));
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.invisibilityOfElementLocated(By.id(ID_CUSTOM_MO_DESC.replace("x", String.valueOf(index)))));
    }

    @Loggable
    public void clickDeleteModelOption(String modelOptionCode) {
        String modelOptionId = ID_MODEL_OPTION_CODE_PREFIX + modelOptionCode;
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", webDriver.findElement(By.id(modelOptionId)));
        this.click(webDriver.findElement(By.cssSelector("#" + modelOptionId + " #" + ID_MODEL_OPTION_DELETE_ICON)));
    }

    @Loggable
    public boolean isConflictDialogDisplayed() {
        try {
            new WebDriverWait(webDriver, 2).until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_MODEL_OPTION_CONFLICT_DIALOG)));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    @Loggable
    public boolean isPartListedOnConflictDialog(String guideNo) {
        try {
            new WebDriverWait(webDriver, 2).until(ExpectedConditions.visibilityOfElementLocated(By.id("conflict-option-" + guideNo)));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    @Loggable
    public boolean isPartListedOnModelOption(String modelOptionId) {
        try {
            new WebDriverWait(webDriver, 2).until(ExpectedConditions.visibilityOfElementLocated(By.id(modelOptionId)));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    @Loggable
    public void confirmConflictDialog() {
        this.click(continueBtn);
        waitForElementNotDisplay(By.id(ID_MODEL_OPTION_CONFLICT_DIALOG));
    }

    public void clickEditModelOption(String modelOptionCode) {
        String modelOptionId = ID_MODEL_OPTION_CODE_PREFIX + modelOptionCode;
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", webDriver.findElement(By.id(modelOptionId)));
        this.click(webDriver.findElement(By.cssSelector("#" + modelOptionId + " #" + ID_MODEL_OPTION_EDIT_ICON)));
    }

    @Loggable
    public void inputEditedModelOptionDescription(String text) { this.sendKeys(editedModelOptionDescriptionInput, text);}

    @Loggable
    public void clickEditedModelOptionContinueButton() {
        this.click(editedModelOptionContinueBtn);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.invisibilityOfElementLocated(By.id(ID_MODEL_OPTION_EDIT_CONTINUE_BTN)));
    }

    @Loggable
    public void clickEditedModelOptionCancelButton() {
        this.click(editedModelOptionCancelBtn);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.invisibilityOfElementLocated(By.id(ID_MODEL_OPTION_EDIT_CANCEL_BTN)));
    }

    @Loggable
    public int getSelectedModelOptionsCount() { return modelOptionGroup.size(); }

    @Loggable
    public void clickModelOptionZone(String zone) {
        for(WebElement element: modelOptionZones) {
            if (element.getText().equals(zone))
                this.click(element);
        }
    }

    @Loggable
    public void selectModelOption(String code) {
        WebElement modelOption = new WebDriverWait(webDriver, 3).until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_SELECT_MODEL_OPTION_CODE_PREFIX + code)));
        this.click(modelOption);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.attributeContains(modelOption, GET_ATTRIBUTE_CLASS, "selected"));
    }

    //Photos
    @Loggable
    public void clickUploadPhotosCenterBtn(String filepath) {
        this.uploadPhotosCenterBtn.sendKeys(filepath);
        waitForElementInvisible(INSIDE_LOADING_CIRCLE);
    }

    @Loggable
    public void clickViewAll() {
        this.click(viewAll);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.attributeContains(viewAll, GET_ATTRIBUTE_CLASS, "active"));
    }

    @Loggable
    public void clickViewByZone() {
        this.click(viewbyZone);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.attributeContains(viewbyZone, GET_ATTRIBUTE_CLASS, "active"));
    }

    @Loggable
    public void clickViewByPart() {
        this.click(viewbyPart);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.attributeContains(viewbyPart, GET_ATTRIBUTE_CLASS, "active"));
    }

    @Loggable
    public boolean isAtViewAll() {
        return viewAll.getAttribute(GET_ATTRIBUTE_CLASS).contains("active");
    }

    @Loggable
    public boolean isAtViewByZone() {
        return viewbyZone.getAttribute(GET_ATTRIBUTE_CLASS).contains("active");
    }

    @Loggable
    public boolean isAtViewByPart() {
        return viewbyPart.getAttribute(GET_ATTRIBUTE_CLASS).contains("active");
    }

    /**
     * @param photoID  photo ID string
     * @param categoryTitle  Category name without "(x)" string which indicates total number of photos
     */
    @Loggable
    public boolean isPhotoFoundInCategory(String photoID, String categoryTitle) {
        Map<String, List> sortedPhotos = getAllCategoryAndPhotoID();
        for (Map.Entry<String, List> entry : sortedPhotos.entrySet()) {
            if (entry.getKey().equals(categoryTitle)) {
                if (entry.getValue().contains(photoID)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Loggable
    public int getPhotoNumbers() {
        return webDriver.findElements(By.className(CLASS_PHOTO_HOLDER)).size();
    }

    @Loggable
    public String getPhotoIDAtViewAll(int index) {
        if (!isAtViewAll()) {
            clickViewAll();
        }
        return webDriver.findElement(By.cssSelector(".img-content[data-index=\"" + (index - 1) + "\"]")).getAttribute("data-itemid");
    }

    /**
     * @param category  Category container or title element that can be used to find photo ID elements
     */
    @Loggable
    private List getAllPhotoIDByCategory(WebElement category) {
        List<String> photoIDList = new ArrayList<>();
        for (WebElement ele : category.findElements(By.className("img-content"))) {
            photoIDList.add(ele.getAttribute("data-itemid"));
        }
        return photoIDList;
    }

    @Loggable
    private Map<String, List> getAllCategoryAndPhotoID() {
        //Store category and photo IDs within it into a map
        Map<String, List> sortedPhotos = new HashMap<>();
        for (WebElement sortContainerEle : viewSortContainer) {
            sortedPhotos.put(getCategory(sortContainerEle), getAllPhotoIDByCategory(sortContainerEle));
        }
        return sortedPhotos;
    }

    @Loggable
    private String getCategory(WebElement sortContainer) {
        //Get category title without "(x)" string next to it
        return sortContainer.findElement(By.cssSelector(CSS_VIEW_SORT_TITLE)).getText().trim().split("\\(")[0];
    }
}
