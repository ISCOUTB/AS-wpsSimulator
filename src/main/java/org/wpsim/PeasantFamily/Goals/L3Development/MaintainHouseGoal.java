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
import org.wpsim.WellProdSim.Base.wpsGoalBDI;
import org.wpsim.WellProdSim.wpsStart;
import org.wpsim.PeasantFamily.Data.PeasantFamilyBelieves;
import org.wpsim.PeasantFamily.Data.Utils.TimeConsumedBy;
import org.wpsim.PeasantFamily.Tasks.L3Development.MaintainHouseTask;
import rational.RationalRole;
import rational.mapping.Believes;
import rational.mapping.Plan;

/**
 *
 * @author jairo
 */
public class MaintainHouseGoal extends wpsGoalBDI {

    /**
     *
     * @return
     */
    public static MaintainHouseGoal buildGoal() {
        MaintainHouseTask maintainHouseTask = new MaintainHouseTask();
        Plan maintainHousePlan = new Plan();
        maintainHousePlan.addTask(maintainHouseTask);
        RationalRole maintainHouseRole = new RationalRole(
                "MaintainHouseTask",
                maintainHousePlan);
        return new MaintainHouseGoal(
                wpsStart.getPlanID(),
                maintainHouseRole,
                "MaintainHouseTask",
                GoalBDITypes.OPORTUNITY);
    }

    /**
     *
     * @param id
     * @param role
     * @param description
     * @param type
     */
    public MaintainHouseGoal(long id, RationalRole role, String description, GoalBDITypes type) {
        super(id, role, description, type);
        //wpsReport.info("");
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
        //wpsReport.info("getHousingCondition=" + believes.getProfile().getHousingCondition());
        if (believes.getPeasantProfile().getHousingCondition() < 1) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     *
     * @param parameters
     * @return
     * @throws KernellAgentEventExceptionBESA
     */
    @Override
    public double evaluatePlausibility(Believes parameters) throws KernellAgentEventExceptionBESA {
        //wpsReport.info("");
        PeasantFamilyBelieves believes = (PeasantFamilyBelieves) parameters;
        //wpsReport.info("isBusy=" + believes.getProfile().isBusy());
        if (believes.haveTimeAvailable(TimeConsumedBy.MaintainHouseTask)) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     *
     * @param parameters
     * @return
     * @throws KernellAgentEventExceptionBESA
     */
    @Override
    public double evaluateViability(Believes parameters) throws KernellAgentEventExceptionBESA {
        //wpsReport.info("");
        PeasantFamilyBelieves believes = (PeasantFamilyBelieves) parameters;
        //wpsReport.info("getTools=" + believes.getProfile().getTools());
        if (believes.getPeasantProfile().getTools() > 0) {
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
        return evaluateEmotionalContribution(stateBDI, 0.6);
    }


}
