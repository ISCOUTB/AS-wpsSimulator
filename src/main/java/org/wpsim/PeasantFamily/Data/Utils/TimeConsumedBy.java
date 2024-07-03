/**
 * ==========================================================================
 * __      __ _ __   ___  *    WellProdSim                                  *
 * \ \ /\ / /| '_ \ / __| *    @version 1.0                                 *
 *  \ V  V / | |_) |\__ \ *    @since 2023                                  *
 *   \_/\_/  | .__/ |___/ *                                                 *
 *           | |          *    @author Jairo Serrano                        *
 *           |_|          *    @author Enrique Gonzalez                     *
 * ==========================================================================
 * Social Simulator used to estimate productivity and well-being of peasant *
 * families. It is event oriented, high concurrency, heterogeneous time     *
 * management and emotional reasoning BDI.                                  *
 * ==========================================================================
 */
package org.wpsim.PeasantFamily.Data.Utils;

/**
 *
 * @author jairo
 */
public enum TimeConsumedBy {

    Void(0),
    DoVoidTask(0),
    LookForLoanTask(240),
    SelfEvaluationTask(120),
    SearchForHelpAndNecessityTask(360),
    SeekPurposeTask(720),
    GetPriceListTask(60),
    DeforestingLandTask(360),
    DoHealthCareTask(720),
    DoVitalsTask(720),
    AttendReligiousEventsTask(120),
    AttendToLivestockTask(120),
    CheckCropsTask(4),
    PlantCropTask(360),
    PrepareLandTask(360),
    HarvestCropsTask(360),
    ManagePestsTask(120),
    AskForCollaborationTask(120),
    IrrigateCropsTask(360),
    MaintainHouseTask(120),
    ProcessProductsTask(360),
    SellCropTask(360),
    SellProductsTask(120),
    SpendFamilyTimeTask(120),
    GetTrainingTask(120),
    ObtainALandTask(360),
    ObtainLivestockTask(240),
    ObtainPesticidesTask(240),
    ObtainSeedsTask(240),
    ObtainSuppliestask(240),
    ObtainToolsTask(240),
    ObtainWaterTask(240),
    CommunicateTask(120),
    CollaborateTask(120),
    LookForCollaborationTask(120),
    ProvideCollaborationTask(120),
    LeisureActivitiesTask(120),
    FindNewsTask(120),
    WasteTimeAndResourcesTask(120),
    PeasantPayDebtsTask(60),
    SpendFriendsTimeTask(120),
    WorkForOtherTask(720),
    AlternativeWorkTask(360),
    GetTechAssistancePlan(240);
    
    private int time;

    private TimeConsumedBy(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
