//
//  MainViewController.swift
//  iosApp
//
//  Created by Aleksey Mikhailov on 18.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import UIKit
import shared

class MainViewController: UIViewController {
    @IBOutlet private var outputText: UITextView!
    @IBOutlet private var actionBtn: UIButton!
    @IBOutlet private var goToSettingsBtn: UIButton!
    
    private var viewModel: MainViewModel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        viewModel = MainViewModel(
            permissionsController: PermissionsController()
        )
        
        viewModel.state.subscribe { [weak self] state in
            let stateKs = MainViewModelStateKs(state!)
            self?.bindState(stateKs)
        }
        
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(willEnterForeground),
            name: UIApplication.willEnterForegroundNotification,
            object: nil
        )
    }
    
    @objc private func willEnterForeground() {
        viewModel.onResume()
    }
    
    @IBAction private func onButtonPressed() {
        viewModel.onButtonPressed()
    }
    
    @IBAction func toSettingsPressed(_ sender: UIButton) {
        let settingsUrl = URL(string: UIApplication.openSettingsURLString)!
        UIApplication.shared.open(settingsUrl)
    }
    
    private func bindState(_ state: MainViewModelStateKs) {
        outputText.text = state.sealed.message
        goToSettingsBtn.isHidden = state != .deniedAlways
        actionBtn.isEnabled = state != .deniedAlways
    }
}
