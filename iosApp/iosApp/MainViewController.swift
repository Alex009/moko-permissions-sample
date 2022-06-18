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
    
    private var viewModel: MainViewModel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        viewModel = MainViewModel()
        
        outputText.bindText(flow: viewModel.resultText)
    }
    
    @IBAction private func onButtonPressed() {
        viewModel.onButtonPressed()
    }
}
