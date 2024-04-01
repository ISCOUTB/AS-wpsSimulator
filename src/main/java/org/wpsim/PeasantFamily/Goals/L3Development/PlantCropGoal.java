/**
 * ==========================================================================
 * __      __ _ __   ___  *    WellProdSim                                  *
 * \ \ /\ / /| '_ \ / __| *    @version 1.0                                 *
 * \ V  V / | |_) |\__ \ *    @since 2023                                  *
 * \_/\_/  | .__/ |___/ *                                                 *
 * | |          *    @author Jairo Serrano                        *
 * |_|          *    @author Enrique Gonzalez                     *
 * ==========================================================================
 * Social Simulator used to estimate productivity and well-being of peasant *
 * families. It is event oriented, high concurrency, heterogeneous time     *
 * management and emotional reasoning BDI.                                  *
 * ==========================================================================
 */
package org.wpsim.PeasantFamily.Goals.L3Development;

import BESA.BDI.AgentStructuralModel.GoalBDITypes;
import BESA.BDI.AgentStructuralModel.StateBDI;
import BESA.Kernel.Agent.Event.KernellAgentEventExceptionBESA;
import org.wpsim.CivicAuthority.Data.LandInfo;
import org.wpsim.WellProdSim.Base.wpsGoalBDI;
import org.wpsim.WellProdSim.wpsStart;
import org.wpsim.PeasantFamily.Data.PeasantFamilyBelieves;
import org.wpsim.PeasantFamily.Data.Utils.SeasonType;
import org.wpsim.PeasantFamily.Data.Utils.TimeConsumedBy;
import org.wpsim.PeasantFamily.Tasks.L3Development.PlantCropTask;
import rational.RationalRole;
import rational.mapping.Believes;
import rational.mapping.Plan;

import java.util.List;

/**
 * @author jairo
 */
public class PlantCropGoal extends wpsGoalBDI {

    /**
     * @return PlantCropGoal
     */
    public static PlantCropGoal buildGoal() {
        PlantCropTask plantCropTask = new PlantCropTask();
        Plan plantCropPlan = new Plan();
        plantCropPlan.addTask(plantCropTask);
        RationalRole plantCropRole = new RationalRole(
                "PlantCropTask",
                plantCropPlan);
        return new PlantCropGoal(
                wpsStart.getPlanID(),
                plantCropRole,
                "PlantCropTask",
                GoalBDITypes.OPORTUNITY);
    }

    /**
     * @param id
     * @param role
     * @param description
     * @param type
     */
    public PlantCropGoal(long id, RationalRole role, String description, GoalBDITypes type) {
        super(id, role, description, type);
    }

    /**
     * @param parameters
     * @return
     * @throws KernellAgentEventExceptionBESA
     */
    @Override
    public double detectGoal(Believes parameters) throws KernellAgentEventExceptionBESA {
        PeasantFamilyBelieves believes = (PeasantFamilyBelieves) parameters;

        if (this.isAlreadyExecutedToday(believes)) {
            return 0;
        }

        List<LandInfo> landInfos = believes.getAssignedLands();
        for (LandInfo currentLandInfo : landInfos) {
            if (currentLandInfo.getCurrentSeason().equals(SeasonType.PLANTING) &&
                    believes.haveTimeAvailable(TimeConsumedBy.PlantCropTask)) {
                if (believes.getPeasantProfile().getSeeds() >= believes.getPeasantProfile().getSeedsNeeded()
                        && believes.getPeasantProfile().getTools() >= believes.getPeasantProfile().getToolsNeeded()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
        return 0;
    }

    /**
     * @param stateBDI
     * @return
     * @throws KernellAgentEventExceptionBESA
     */
    @Override
    public double evaluateContribution(StateBDI stateBDI) throws KernellAgentEventExceptionBESA {
        return evaluateEmotionalContribution(stateBDI, 0.98);
    }

}
