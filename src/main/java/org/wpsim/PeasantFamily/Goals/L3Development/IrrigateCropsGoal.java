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
import org.wpsim.PeasantFamily.Data.Utils.CropCareType;
import org.wpsim.PeasantFamily.Data.Utils.TimeConsumedBy;
import org.wpsim.PeasantFamily.Tasks.L3Development.IrrigateCropsTask;
import rational.RationalRole;
import rational.mapping.Believes;
import rational.mapping.Plan;

/**
 *
 * @author jairo
 */
public class IrrigateCropsGoal extends wpsGoalBDI {

    /**
     * Builds IrrigateCropsGoal
     * @return IrrigateCropsGoal with default values
     */
    public static IrrigateCropsGoal buildGoal() {
        IrrigateCropsTask irrigateCropsTask = new IrrigateCropsTask();
        Plan irrigateCropsPlan = new Plan();
        irrigateCropsPlan.addTask(irrigateCropsTask);
        RationalRole irrigateCropsRole = new RationalRole(
                "IrrigateCropsTask",
                irrigateCropsPlan);
        return new IrrigateCropsGoal(
                wpsStart.getPlanID(),
                irrigateCropsRole,
                "IrrigateCropsTask",
                GoalBDITypes.OPORTUNITY);
    }

    /**
     *
     * @param id
     * @param role
     * @param description
     * @param type
     */
    public IrrigateCropsGoal(long id, RationalRole role, String description, GoalBDITypes type) {
        super(id, role, description, type);
    }

    /**
     *
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

        for (LandInfo currentLandInfo : believes.getAssignedLands()) {
            if (currentLandInfo.getCurrentCropCareType().equals(CropCareType.IRRIGATION) &&
                    believes.haveTimeAvailable(TimeConsumedBy.IrrigateCropsTask)) {
                int waterUsed = (int) ((believes.getPeasantProfile().getCropSizeHA()) * 30);
                for (LandInfo currentLandInfoWater : believes.getAssignedLands()) {
                    if (currentLandInfoWater.getKind().equals("water") ||
                            believes.getPeasantProfile().getWaterAvailable() >= waterUsed) {
                        return 1;
                    }
                }
            }
        }
        return 0;
    }

    /**
     *
     * @param stateBDI
     * @return
     * @throws KernellAgentEventExceptionBESA
     */
    @Override
    public double evaluateContribution(StateBDI stateBDI) throws KernellAgentEventExceptionBESA {
        return evaluateEmotionalContribution(stateBDI, 0.98);
    }

}
