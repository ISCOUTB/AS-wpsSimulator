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
package org.wpsim.PeasantFamily.Goals.L3Development;

import BESA.BDI.AgentStructuralModel.GoalBDITypes;
import BESA.BDI.AgentStructuralModel.StateBDI;
import BESA.Kernel.Agent.Event.KernellAgentEventExceptionBESA;
import org.wpsim.CivicAuthority.Data.LandInfo;
import org.wpsim.PeasantFamily.Data.PeasantFamilyBelieves;
import org.wpsim.PeasantFamily.Data.Utils.TimeConsumedBy;
import org.wpsim.WellProdSim.Base.wpsGoalBDI;
import org.wpsim.PeasantFamily.Tasks.L3Development.DeforestingLandTask;
import org.wpsim.WellProdSim.wpsStart;
import rational.RationalRole;
import rational.mapping.Believes;
import rational.mapping.Plan;

/**
 *
 * @author jairo
 */
public class DeforestingLandGoal extends wpsGoalBDI {

    /**
     *
     * @return
     */
    public static DeforestingLandGoal buildGoal() {
        DeforestingLandTask deforestingLandTask = new DeforestingLandTask();
        Plan deforestingLandPlan = new Plan();
        deforestingLandPlan.addTask(deforestingLandTask);
        RationalRole deforestingLandRole = new RationalRole(
                "DeforestingLandTask",
                deforestingLandPlan);
        return new DeforestingLandGoal(
                wpsStart.getPlanID(),
                deforestingLandRole,
                "DeforestingLandTask",
                GoalBDITypes.OPORTUNITY);
    }

    /**
     * Creates the DeforestingLandGoal
     * @param id
     * @param role
     * @param description
     * @param type
     */
    public DeforestingLandGoal(long id, RationalRole role, String description, GoalBDITypes type) {
        super(id, role, description, type);
    }

    /**
     *
     * @param parameters
     * @return
     * @throws KernellAgentEventExceptionBESA
     */
    @Override
    public double evaluateViability(Believes parameters) throws KernellAgentEventExceptionBESA {
        return 1;
    }

    /**
     * Detects the goal of the peasant family
     * @param parameters of the peasant family
     * @return 1 if the peasant family has enough time to deforest the land
     * @throws KernellAgentEventExceptionBESA
     */
    @Override
    public double detectGoal(Believes parameters) throws KernellAgentEventExceptionBESA {
        PeasantFamilyBelieves believes = (PeasantFamilyBelieves) parameters;

        if (this.isAlreadyExecutedToday(believes)) {
            return 0;
        }

        for (LandInfo currentLandInfo : believes.getAssignedLands()) {
            if (currentLandInfo.getKind().equals("forest")) {
                return 1;
            }
        }
        return 0;
    }

    /**
     *
     * @param parameters
     * @return
     * @throws KernellAgentEventExceptionBESA
     */
    @Override
    public double evaluatePlausibility(Believes parameters) throws KernellAgentEventExceptionBESA {
        PeasantFamilyBelieves believes = (PeasantFamilyBelieves) parameters;
        if (believes.haveTimeAvailable(TimeConsumedBy.DeforestingLandTask)) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     *
     * @param stateBDI
     * @return
     * @throws KernellAgentEventExceptionBESA
     */
    @Override
    public double evaluateContribution(StateBDI stateBDI) throws KernellAgentEventExceptionBESA {
        return evaluateEmotionalContribution(stateBDI, 1.0);
    }


}
